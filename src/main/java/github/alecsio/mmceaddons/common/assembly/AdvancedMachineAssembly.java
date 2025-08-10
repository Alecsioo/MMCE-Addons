package github.alecsio.mmceaddons.common.assembly;

import appeng.api.AEApi;
import appeng.api.IAppEngApi;
import appeng.api.config.Actionable;
import appeng.api.config.FuzzyMode;
import appeng.api.features.ILocatable;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.IActionSource;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.IStorageHelper;
import appeng.api.storage.channels.IFluidStorageChannel;
import appeng.api.storage.channels.IItemStorageChannel;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.storage.data.IAEStack;
import appeng.fluids.util.AEFluidStack;
import appeng.me.helpers.PlayerSource;
import appeng.util.item.AEItemStack;
import com.google.common.collect.ImmutableList;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.LoadedModsCache;
import github.alecsio.mmceaddons.common.assembly.handler.MachineAssemblyEventHandler;
import github.alecsio.mmceaddons.common.assembly.handler.exception.MultiblockBuilderNotFoundException;
import github.alecsio.mmceaddons.common.config.MMCEAConfig;
import github.alecsio.mmceaddons.common.item.ItemAdvancedMachineAssembler;
import ink.ikx.mmce.common.utils.FluidUtils;
import ink.ikx.mmce.common.utils.StructureIngredient;
import moze_intel.projecte.api.ProjectEAPI;
import moze_intel.projecte.api.capabilities.IKnowledgeProvider;
import moze_intel.projecte.utils.EMCHelper;
import net.minecraft.block.Block;
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
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.Optional;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import stanhebben.zenscript.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Represents and encapsulates all the logic related to the assembly of a multiblock.
 * Assemblies are managed by {@link github.alecsio.mmceaddons.common.assembly.MachineAssemblyManager}, which acts as
 * a cache for and provider of scheduled assemblies.
 * Assemblies are added to the manager from {@link ItemAdvancedMachineAssembler#onItemUse(EntityPlayer, World, BlockPos, EnumHand, EnumFacing, float, float, float)}
 * and an assembly step is triggered every player tick from {@link MachineAssemblyEventHandler#onPlayerTick(TickEvent.PlayerTickEvent)}
 */
public class AdvancedMachineAssembly extends AbstractMachineAssembly {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int AMOUNT_TO_EXTRACT = 1;

    public AdvancedMachineAssembly(World world, BlockPos controllerPos, EntityPlayer player, StructureIngredient ingredient) {
        super(world, controllerPos, player, ingredient);
    }

    /**
     * Processes up to {@link MMCEAConfig#disassemblyBlocksProcessedPerTick} blocks from the assembly
     */
    @Override
    public void assembleTick() throws MultiblockBuilderNotFoundException {
        List<StructureIngredient.ItemIngredient> itemIngredients = ingredient.itemIngredient();
        List<StructureIngredient.FluidIngredient> fluidIngredients = ingredient.fluidIngredient();

        Iterator<StructureIngredient.ItemIngredient> itemIterator = itemIngredients.iterator();
        Iterator<StructureIngredient.FluidIngredient> fluidIterator = fluidIngredients.iterator();

        if (player.isCreative()) {
            handleCreativeAssembly();
            return;
        }

        for (int i = 0; i < MMCEAConfig.assemblyBlocksProcessedPerTick; i++) {
            if (!itemIterator.hasNext()) {
                break;
            }
            StructureIngredient.ItemIngredient ingredientToProcess = itemIterator.next();
            tryPlaceBlock(ingredientToProcess);
            itemIterator.remove();
        }

        if (!itemIterator.hasNext()) {
            for (int i = 0; i < MMCEAConfig.assemblyBlocksProcessedPerTick; i++) {
                if (!fluidIterator.hasNext()) {
                    completed = true;
                    break;
                }
                StructureIngredient.FluidIngredient fluidIngredient = fluidIterator.next();
                tryPlaceFluid(fluidIngredient);
                fluidIterator.remove();
            }
        }

        sendAndResetError();
    }

    @Override
    public String getCompletedTranslationKey() {
        return hadError ? "message.modularmachineryaddons.assembly.complete.error" : "message.modularmachineryaddons.assembly.complete";
    }

    @Override
    public String getErrorTranslationKey() {
        return "message.modularmachineryaddons.assembly.error";
    }

    @Override
    public String getMissingBlocksTranslationKey() {
        return "message.assembly.tip.success.missing.blocks";
    }

    @Override
    protected void handleCreativeAssembly() {
        List<StructureIngredient.ItemIngredient> itemIngredients = ingredient.itemIngredient();
        List<StructureIngredient.FluidIngredient> fluidIngredients = ingredient.fluidIngredient();

        Iterator<StructureIngredient.ItemIngredient> itemIterator = itemIngredients.iterator();
        Iterator<StructureIngredient.FluidIngredient> fluidIterator = fluidIngredients.iterator();

        while (itemIterator.hasNext()) {
            StructureIngredient.ItemIngredient ingredient = itemIterator.next();
            Tuple<ItemStack, IBlockState> stackStateTuple = ingredient.ingredientList().get(0);
            handleBlockPlacement(getControllerPos().add(ingredient.pos()), stackStateTuple.getSecond(), stackStateTuple.getFirst(), ingredient.nbt());
            itemIterator.remove();
        }

        while (fluidIterator.hasNext()) {
            StructureIngredient.FluidIngredient fluidIngredient = fluidIterator.next();
            Tuple<FluidStack, IBlockState> stackStateTuple = fluidIngredient.ingredientList().get(0);
            BlockPos fluidPos = getControllerPos().add(fluidIngredient.pos());
            world.setBlockState(fluidPos, stackStateTuple.getSecond());
            world.playSound(null, fluidPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }
        completed = true;
    }

    private void tryPlaceFluid(StructureIngredient.FluidIngredient fluidIngredient) throws MultiblockBuilderNotFoundException {
        BlockPos fluidPos = getControllerPos().add(fluidIngredient.pos());

        for (int i = fluidIngredient.ingredientList().size() - 1; i >= 0; i--) {
            Tuple<FluidStack, IBlockState> stackStateTuple = fluidIngredient.ingredientList().get(i);

            FluidStack fluidStack = stackStateTuple.getFirst();
            IBlockState blockState = stackStateTuple.getSecond();

            if (world.getBlockState(fluidPos) == blockState) {
                return;
            }

            if (!canPlaceFluidAt(fluidPos, fluidStack)) {continue;}

            FluidStack handledFluid = null;
            ItemStack assembler = null;
            List<IFluidHandlerItem> fluidHandlers = new ArrayList<>();
            // player inv
            for (final ItemStack stackInSlot : player.inventory.mainInventory) {
                // If there's more than one assembler this will work incorrectly (say if one is linked to the ME system and the other is not)
                // but idk if it's worth addressing
                if (stackInSlot.getItem() instanceof ItemAdvancedMachineAssembler) {
                    assembler = stackInSlot;
                }

                if (FluidUtils.isFluidHandler(stackInSlot)) {
                    fluidHandlers.add(FluidUtil.getFluidHandler(stackInSlot));
                }
            }

            if (assembler == null) {
                throw new MultiblockBuilderNotFoundException();
            }

            for (IFluidHandlerItem fluidHandler : fluidHandlers) {
                FluidStack drained = fluidHandler.drain(fluidStack, false);
                if (drained != null && drained.isFluidEqual(fluidStack)) {
                    handledFluid = drained.copy();
                    fluidHandler.drain(fluidStack, true);
                    break;
                }
            }

            AssemblyModes mode = getAssemblyModesFrom(assembler);

            if (handledFluid == null && LoadedModsCache.aeLoaded && mode.supports(AssemblySupportedMods.APPLIEDENERGISTICS2)) {
                handledFluid = canMEHandle(fluidStack, assembler);
            }

            if (handledFluid == null) {
                if (i == 0) {
                    unhandledFluids.add(new FluidStack(fluidStack.getFluid(), 1, fluidStack.tag));
                    hadError = true;
                }
                continue;
            }

            lastError = null;

            world.setBlockState(fluidPos, blockState);
            world.playSound(null, fluidPos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
            break;
        }
    }

    /**
     * Tries to place the provided ingredient. This method will look for the provided ingredient in the following
     * locations, in this order:
     * - Player inventory. This is also needed to find an {@link ItemAdvancedMachineAssembler} in the inventory,
     *   which is needed for the next steps
     * - From the linked ME system, if the mod is present and the assembler is linked to an ME system
     * - From the EMC network, if the mod is present
     */
    private void tryPlaceBlock(StructureIngredient.ItemIngredient ingredientToProcess) throws MultiblockBuilderNotFoundException {
        BlockPos toPlacePos = getControllerPos().add(ingredientToProcess.pos());

        if (!canPlaceBlockAt(toPlacePos)) {
            unhandledBlocks.add(ingredientToProcess.ingredientList().get(0).getFirst().copy());
            return;
        }

        // The ingredient list contains a list of all valid blocks for the given block pos. For example, the different tiers
        // of hatches
        for (int i = ingredientToProcess.ingredientList().size() - 1; i >= 0; i--) {
            Tuple<ItemStack, IBlockState> stackStateTuple = ingredientToProcess.ingredientList().get(i);

            ItemStack stack = stackStateTuple.getFirst();
            IBlockState state = stackStateTuple.getSecond();

            ItemStack handledItem = EMPTY;
            ItemStack assembler = null;
            ItemStack stackInInvToConsume = null;
            // player inv
            for (final ItemStack stackInSlot : player.inventory.mainInventory) {
                // If there's more than one assembler this will work incorrectly (say if one is linked to the ME system and the other is not)
                // but idk if it's worth addressing
                if (stackInSlot.getItem() instanceof ItemAdvancedMachineAssembler) {
                    assembler = stackInSlot;
                    if (handledItem != EMPTY) {break;}
                }

                // Handling both the assembler and block lookup in the same loop means that even if you don't have an assembler
                // it will still place items from your inventory. This happens if, for example, you start an assembly and throw the item.
                if (ItemStack.areItemsEqual(stack, stackInSlot) && handledItem == EMPTY) {
                    stackInInvToConsume = stackInSlot;
                    handledItem = stackInInvToConsume.copy();
                }
            }

            if (assembler == null) {
                throw new MultiblockBuilderNotFoundException();
            }

            if (stackInInvToConsume != null) {
                stackInInvToConsume.shrink(stack.getCount());
            }

            AssemblyModes mode = getAssemblyModesFrom(assembler);

            if (handledItem == EMPTY && LoadedModsCache.aeLoaded && mode.supports(AssemblySupportedMods.APPLIEDENERGISTICS2)) {
                handledItem = canMEHandle(stack, assembler);
            }

            if (handledItem == EMPTY && LoadedModsCache.projecteLoaded && mode.supports(AssemblySupportedMods.PROJECTE)) {
                handledItem = canEMCHandle(stack);
            }

            if (handledItem == EMPTY) {
                if (i == 0) {
                    unhandledBlocks.add(stack.copy());
                    hadError = true;
                }
                continue;
            }

            lastError = null;

            handleBlockPlacement(toPlacePos, state, handledItem, ingredientToProcess.nbt());
            break;
        }
    }

    private void handleBlockPlacement(BlockPos toPlacePos, IBlockState state, ItemStack handledItem, NBTTagCompound tag) {
        IBlockState oldState = world.getBlockState(toPlacePos);
        world.setBlockState(toPlacePos, state);
        Block block = world.getBlockState(toPlacePos).getBlock();
        // For some reason pillars (specifically TC eldritch pillars are placed with a different facing direction if this is called on them
        block.onBlockPlacedBy(world, toPlacePos, state, player, handledItem);
        world.markAndNotifyBlock(toPlacePos, null, oldState, state, Constants.BlockFlags.DEFAULT);
        world.playSound(null, toPlacePos, SoundEvents.BLOCK_METAL_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        TileEntity te = world.getTileEntity(toPlacePos);

        if (te != null && tag != null) {
            try {
                te.readFromNBT(tag);
            } catch (Exception e) {
                ModularMachineryAddons.logger.warn("Failed to apply NBT to TileEntity!", e);
                world.removeTileEntity(toPlacePos);
                world.setTileEntity(toPlacePos, state.getBlock().createTileEntity(world, state));
            }
        }
    }

    @Optional.Method(modid = LoadedModsCache.AE2)
    private @Nullable FluidStack canMEHandle(FluidStack stack, ItemStack assembler) {
        IAppEngApi aeApi = AEApi.instance();
        java.util.Optional<Long> optEncryptionKey = getOptionalEncryptionKeyFrom(assembler);
        if (!optEncryptionKey.isPresent()) {
            return null;
        }

        long parsedKey = optEncryptionKey.get();
        ILocatable securityStation = aeApi.registries().locatable().getLocatableBy(parsedKey);
        if (!(securityStation instanceof IActionHost host)) {
            lastError = new TextComponentTranslation("error.security.station.not.found");
            return null;
        }

        IGridNode node = host.getActionableNode();
        IGrid targetGrid = node.getGrid();
        IEnergyGrid energyGrid = targetGrid.getCache(IEnergyGrid.class);
        IStorageGrid storageGrid = targetGrid.getCache(IStorageGrid.class);
        IActionSource playerSource = new PlayerSource(player, host);

        IStorageHelper storageHelper = aeApi.storage();

        IMEMonitor<IAEFluidStack> fluidStorage = storageGrid.getInventory(storageHelper.getStorageChannel(IFluidStorageChannel.class));
        IAEFluidStack fluidStack = AEFluidStack.fromFluidStack(stack);

        IAEStack<IAEFluidStack> aeStack = aeApi.storage().poweredExtraction(energyGrid, fluidStorage, fluidStack, playerSource, Actionable.SIMULATE);

        if (aeStack != null && aeStack.getStackSize() == stack.amount) {
            storageHelper.poweredExtraction(energyGrid, fluidStorage, fluidStack, playerSource, Actionable.MODULATE);
            return stack.copy();
        }

        return null;
    }

    @Optional.Method(modid = LoadedModsCache.AE2)
    private ItemStack canMEHandle(ItemStack stack, @NotNull ItemStack assembler) {
        IAppEngApi aeApi = AEApi.instance();
        java.util.Optional<Long> optEncryptionKey = getOptionalEncryptionKeyFrom(assembler);
        if (!optEncryptionKey.isPresent()) {
            return EMPTY;
        }

        long parsedKey = optEncryptionKey.get();
        ILocatable securityStation = aeApi.registries().locatable().getLocatableBy(parsedKey);
        if (!(securityStation instanceof IActionHost host)) {
            lastError = new TextComponentTranslation("error.security.station.not.found");
            return EMPTY;
        }

        IGridNode node = host.getActionableNode();
        IGrid targetGrid = node.getGrid();
        IEnergyGrid energyGrid = targetGrid.getCache(IEnergyGrid.class);
        IStorageGrid storageGrid = targetGrid.getCache(IStorageGrid.class);
        IActionSource playerSource = new PlayerSource(player, host);

        IStorageHelper storageHelper = aeApi.storage();

        IMEMonitor<IAEItemStack> itemStorage = storageGrid.getInventory(storageHelper.getStorageChannel(IItemStorageChannel.class));

        NBTTagCompound stackTag = stack.getTagCompound();
        AEItemStack aeItemStack = AEItemStack.fromItemStack(stack);

        // If we don't need to match exact NBT for this block
        if (stackTag == null) {
            for (final IAEItemStack toExtract : ImmutableList.copyOf(itemStorage.getStorageList().findFuzzy(aeItemStack, FuzzyMode.IGNORE_ALL))) {
                if (toExtract.getStackSize() >= AMOUNT_TO_EXTRACT) {
                    // The only interaction with itemStorage directly is to get the stack to extract
                    ItemStack matchedStack = toExtract.getCachedItemStack(toExtract.getStackSize());
                    // So once I get it, I just put it back in the cached item stack to avoid inconsistencies
                    // the actual extraction will be handled by poweredExtraction
                    toExtract.setCachedItemStack(matchedStack.copy());

                    // Check meta, else it could end up placing a different block, this totally did not happen during development
                    if (matchedStack.getMetadata() != stack.getMetadata()) {
                        continue;
                    }

                    matchedStack.setCount(AMOUNT_TO_EXTRACT);
                    aeItemStack = AEItemStack.fromItemStack(matchedStack);
                    break;
                }
            }
        }

        IAEStack<IAEItemStack> aeStack = aeApi.storage().poweredExtraction(energyGrid, itemStorage, aeItemStack, playerSource, Actionable.SIMULATE);

        if (aeStack != null && aeStack.getStackSize() == stack.getCount()) {
            IAEStack<IAEItemStack> extracted = storageHelper.poweredExtraction(energyGrid, itemStorage, aeItemStack, playerSource, Actionable.MODULATE);
            return extracted.asItemStackRepresentation();
        }

        return EMPTY;
    }

    @Optional.Method(modid = LoadedModsCache.PROJECTE)
    private ItemStack canEMCHandle(ItemStack stack) {
        if (player == null) {
            LOGGER.error("Null player in assembly. This should probably not happen.");
            return EMPTY;
        }

        IKnowledgeProvider provider = player.getCapability(ProjectEAPI.KNOWLEDGE_CAPABILITY, null);
        if (provider == null) {
            LOGGER.error("Null IKnowledgeProvider in assembly. This should probably not happen.");
            return EMPTY;
        }

        boolean hasEmc = EMCHelper.doesItemHaveEmc(stack);
        if (!hasEmc) {
            return EMPTY;
        }

        long emc = provider.getEmc();
        long stackEmcValue = EMCHelper.getEmcValue(stack);

        if (!provider.hasKnowledge(stack)) {
            lastError = new TextComponentTranslation("message.assembly.missing.item.knowledge", stack.getDisplayName());
            return EMPTY;
        }

        if (emc - stackEmcValue < 0) {
            lastError = new TextComponentTranslation("message.assembly.missing.emc", stack.getDisplayName(), stack.getCount(), emc, stackEmcValue);
            return EMPTY;
        }

        provider.setEmc(emc - stackEmcValue);
        return stack;
    }

    private boolean canPlaceFluidAt(BlockPos pos, FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();

        return canPlaceBlockAt(pos) && !(world.provider.doesWaterVaporize() && fluid.doesVaporize(fluidStack));
    }

    private boolean canPlaceBlockAt(BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        if (!player.isAllowEdit() || world.isOutsideBuildHeight(pos) || !world.isBlockModifiable(player, pos) || !state.getBlock().isReplaceable(world, pos) || state.isFullBlock()) {
            return false;
        }

        BlockSnapshot blockSnapshot = BlockSnapshot.getBlockSnapshot(world, pos);

        return !ForgeEventFactory.onPlayerBlockPlace(player, blockSnapshot, EnumFacing.UP, EnumHand.MAIN_HAND).isCanceled();
    }

}
