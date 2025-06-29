package github.alecsio.mmceaddons.common.assembly;

import appeng.api.AEApi;
import appeng.api.IAppEngApi;
import appeng.api.config.Actionable;
import appeng.api.features.ILocatable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageHelper;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.me.helpers.PlayerSource;
import appeng.util.item.AEItemStack;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.LoadedModsCache;
import github.alecsio.mmceaddons.common.assembly.handler.MachineAssemblyEventHandler;
import github.alecsio.mmceaddons.common.config.MMCEAConfig;
import github.alecsio.mmceaddons.common.item.ItemAdvancedMachineAssembler;
import ink.ikx.mmce.common.utils.StructureIngredient;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stanhebben.zenscript.annotations.NotNull;

import java.util.*;

/**
 * Represents and encapsulates all the logic related to the assembly of a multiblock.
 * Assemblies are managed by {@link github.alecsio.mmceaddons.common.assembly.MachineAssemblyManager}, which acts as
 * a cache for and provider of scheduled assemblies.
 * Assemblies are added to the manager from {@link ItemAdvancedMachineAssembler#onItemUse(EntityPlayer, World, BlockPos, EnumHand, EnumFacing, float, float, float)}
 * and an assembly step is triggered every player tick from {@link MachineAssemblyEventHandler#onPlayerTick(TickEvent.PlayerTickEvent)}
 */
public class AdvancedMachineAssembly extends AbstractMachineAssembly {

    private static final Logger LOGGER = LogManager.getLogger();

    // The last error that was reported by any of the handlers. If multiple errors were reported, only the last
    // one will be displayed to the player.
    private TextComponentTranslation lastError;

    public AdvancedMachineAssembly(World world, BlockPos controllerPos, EntityPlayer player, StructureIngredient ingredient) {
        super(world, controllerPos, player, ingredient);
    }

    /**
     * Processes up to {@link MMCEAConfig#blocksProcessedPerTick} blocks from the assembly
     */
    @Override
    public void assembleTick() {
        List<StructureIngredient.ItemIngredient> itemIngredients = ingredient.itemIngredient();
        Iterator<StructureIngredient.ItemIngredient> iterator = itemIngredients.iterator();

        for (int i = 0; i < MMCEAConfig.blocksProcessedPerTick; i++) {
            if (!iterator.hasNext()) {
                completed = true;
                break;
            }
            StructureIngredient.ItemIngredient ingredientToProcess = iterator.next();
            tryPlaceBlock(ingredientToProcess);
            iterator.remove();
        }

        sendAndResetError();
    }

    /**
     * Tries to place the provided ingredient. This method will look for the provided ingredient in the following
     * locations, in this order:
     * - Player inventory. This is also needed to find an {@link ItemAdvancedMachineAssembler} in the inventory,
     *   which is needed for the next steps
     * - From the linked ME system, if the mod is present and the assembler is linked to an ME system
     * - From the EMC network, if the mod is present
     */
    private void tryPlaceBlock(StructureIngredient.ItemIngredient ingredientToProcess) {
        BlockPos toPlacePos = getControllerPos().add(ingredientToProcess.pos());

        if (!canPlaceBlockAt(toPlacePos)) {return;}

        // The ingredient list contains a list of all valid blocks for the given block pos. For example, the different tiers
        // of hatches

        for (Tuple<ItemStack, IBlockState> stackStateTuple : ingredientToProcess.ingredientList()) {
            ItemStack stack = stackStateTuple.getFirst();
            IBlockState state = stackStateTuple.getSecond();

            boolean handled = false;
            ItemStack assembler = null;
            // player inv
            for (final ItemStack stackInSlot : player.inventory.mainInventory) {
                // If there's more than one assembler this will work incorrectly (say if one is linked to the ME system and the other is not)
                // but idk if it's worth addressing
                if (stackInSlot.getItem() instanceof ItemAdvancedMachineAssembler) {
                    assembler = stackInSlot;
                    if (handled) {break;}
                }

                if (ItemStack.areItemsEqual(stack, stackInSlot) && !handled) {
                    handled = true;
                    stackInSlot.shrink(stack.getCount());
                }
            }

            if (assembler == null) {
                lastError = new TextComponentTranslation("error.assembler.not.found");
                return;
            }

            NBTTagCompound tag = assembler.getTagCompound();
            int modeIndex;
            if (tag == null) {
                assembler.setTagCompound(tag = new NBTTagCompound());
            }
            modeIndex = tag.getInteger(ItemAdvancedMachineAssembler.MODE_INDEX);
            AssemblyModes mode = AssemblyModes.getSupportedModes().get(modeIndex);

            if (!handled && LoadedModsCache.aeLoaded && mode.supports(AssemblySupportedMods.APPLIEDENERGISTICS2)) {
                handled = canMEHandle(stack, assembler);
            }

            if (!handled && LoadedModsCache.projecteLoaded && mode.supports(AssemblySupportedMods.PROJECTE)) {
                handled = canEMCHandle(stack);
            }

            if (!handled) {
                continue;
            }

            world.setBlockState(toPlacePos, state);
            world.playSound(null, toPlacePos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            TileEntity te = world.getTileEntity(toPlacePos);
            if (te != null && ingredientToProcess.nbt() != null) {
                try {
                    te.readFromNBT(ingredientToProcess.nbt());
                } catch (Exception e) {
                    ModularMachineryAddons.logger.warn("Failed to apply NBT to TileEntity!", e);
                    world.removeTileEntity(toPlacePos);
                    world.setTileEntity(toPlacePos, state.getBlock().createTileEntity(world, state));
                }
            }
            break;
        }
    }

    @Optional.Method(modid = LoadedModsCache.AE2)
    private boolean canMEHandle(ItemStack stack, @NotNull ItemStack assembler) {
        IAppEngApi aeApi = AEApi.instance();
        java.util.Optional<String> optEncryptionKey = getEncryptionKey(assembler);
        if (!optEncryptionKey.isPresent() || optEncryptionKey.get().isEmpty()) {
            lastError = new TextComponentTranslation("error.encryption.key.not.found");
            return false;
        }

        long parsedKey = Long.parseLong(optEncryptionKey.get());
        ILocatable securityStation = aeApi.registries().locatable().getLocatableBy(parsedKey);
        if (!(securityStation instanceof IActionHost host)) {
            lastError = new TextComponentTranslation("error.security.station.not.found");
            return false;
        }

        // wonder what the performance impact of retrieving all this stuff multiple times for each block is.
        // Not sure if it would be possible to cache some of these things between assembly steps and still guarantee
        // consistency, but I guess it would be safe to cache the IMEMonitor<IAEItemStack> in the same assembly step.
        // so if 16 blocks need to be placed in an assembly step, all this will be retrieved only on the first block,
        // while the 15 other blocks will just use the cached IMEMonitor<IAEItemStack>
        IGridNode node = host.getActionableNode();
        IGrid targetGrid = node.getGrid();
        IEnergyGrid energyGrid = targetGrid.getCache(IEnergyGrid.class);
        IStorageGrid storageGrid = targetGrid.getCache(IStorageGrid.class);

        IStorageHelper storageHelper = aeApi.storage();

        IMEMonitor<IAEItemStack> itemStorage = storageGrid.getInventory(storageHelper.getStorageChannel(IItemStorageChannel.class));

        IAEStack<IAEItemStack> aeStack = aeApi.storage().poweredExtraction(energyGrid, itemStorage, AEItemStack.fromItemStack(stack), new PlayerSource(player, host), Actionable.SIMULATE);
        if (aeStack != null && aeStack.getStackSize() == stack.getCount()) {
            storageHelper.poweredExtraction(energyGrid, itemStorage, AEItemStack.fromItemStack(stack), new PlayerSource(player, host), Actionable.MODULATE);
            return true;
        }

        return false;
    }

    @Optional.Method(modid = LoadedModsCache.PROJECTE)
    private boolean canEMCHandle(ItemStack stack) {
        if (player == null) {
            LOGGER.error("Null player in assembly. This should probably not happen.");
            return false;
        }

        IKnowledgeProvider provider = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
        if (provider == null) {
            LOGGER.error("Null IKnowledgeProvider in assembly. This should probably not happen.");
            return false;
        }

        boolean hasEmc = EMCHelper.doesItemHaveEmc(stack);
        if (!hasEmc) {
            return false;
        }

        long emc = provider.getEmc();
        long stackEmcValue = EMCHelper.getEmcValue(stack);

        if (!provider.hasKnowledge(stack)) {
            lastError = new TextComponentTranslation("message.assembly.missing.item.knowledge", stack.getDisplayName());
            return false;
        }

        if (emc - stackEmcValue < 0) {
            lastError = new TextComponentTranslation("message.assembly.missing.emc", stack.getDisplayName(), stack.getCount(), emc, stackEmcValue);
            return false;
        }

        provider.setEmc(emc - stackEmcValue);
        return true;
    }

    private java.util.Optional<String> getEncryptionKey(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        String key = null;
        if (tagCompound != null) {
            key = tagCompound.getString(ItemAdvancedMachineAssembler.AE2_ENCRYPTION_KEY);
        }
        return java.util.Optional.ofNullable(key);
    }

    @SuppressWarnings("deprecation") // Those warnings are only relevant for 1.13+
    private boolean canPlaceBlockAt(BlockPos pos) {
        BlockSnapshot blockSnapshot = BlockSnapshot.getBlockSnapshot(world, pos);
        // probably double check this and ensure it works with ie claims
        IBlockState placedAgainst = blockSnapshot.getWorld().getBlockState(blockSnapshot.getPos().offset(EnumFacing.DOWN)); // idk?
        BlockEvent.PlaceEvent placeEvent = new BlockEvent.PlaceEvent(BlockSnapshot.getBlockSnapshot(world, pos), placedAgainst, player, EnumHand.MAIN_HAND);
        MinecraftForge.EVENT_BUS.post(placeEvent);
        return !placeEvent.isCanceled();
    }

    private void sendAndResetError() {
        if (lastError != null) {
            player.sendMessage(lastError);
            lastError = null;
        }
    }

}
