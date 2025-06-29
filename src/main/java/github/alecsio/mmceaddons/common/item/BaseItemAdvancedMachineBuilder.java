package github.alecsio.mmceaddons.common.item;

import appeng.api.features.INetworkEncodable;
import appeng.util.Platform;
import github.alecsio.mmceaddons.CommonProxy;
import github.alecsio.mmceaddons.common.assembly.AdvancedMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.AssemblyModes;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.MachineAssemblyManager;
import github.kasuminova.mmce.common.util.DynamicPattern;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.block.BlockFactoryController;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import hellfirepvp.modularmachinery.common.util.BlockArrayCache;
import ink.ikx.mmce.common.utils.StructureIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;

import static github.alecsio.mmceaddons.common.assembly.handler.MachineAssemblyEventHandler.ASSEMBLY_ACCESS_TOKEN;

@Optional.Interface(modid = "appliedenergistics2", iface = "appeng.api.features.INetworkEncodable")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BaseItemAdvancedMachineBuilder extends Item implements INetworkEncodable {

    public static final String AE2_ENCRYPTION_KEY = "encryptionKey";
    public static final String MODE_INDEX = "modeIndex";

    /**
     *      Defines the index of the currently selected mode in the available modes list in {@link AssemblyModes#supportedModes}
     */
    protected int modeIndex = 0;

    public BaseItemAdvancedMachineBuilder() {
        this.setMaxStackSize(1);
        this.setNoRepair();

        setCreativeTab(CommonProxy.creativeTabModularMachineryAddons);
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public String getEncryptionKey(ItemStack item) {
        NBTTagCompound tag = Platform.openNbtData(item);
        return tag.getString(AE2_ENCRYPTION_KEY);
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        NBTTagCompound tag = Platform.openNbtData(item);
        tag.setString(AE2_ENCRYPTION_KEY, encKey);
        tag.setString("name", name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        List<AssemblyModes> modes = AssemblyModes.getSupportedModes();
        NBTTagCompound tag = stack.getTagCompound();
        if (tag != null) {
            tooltip.add("Mode: " + modes.get(tag.getInteger(MODE_INDEX)).name());
        }

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote || playerIn.isSneaking()) {
            return super.onItemRightClick(worldIn, playerIn, handIn);
        }

        ItemStack stack = playerIn.getHeldItem(handIn);
        NBTTagCompound tag = stack.getTagCompound();
        if (tag == null) {
            stack.setTagCompound(tag = new NBTTagCompound());
        }

        List<AssemblyModes> supportedModes = AssemblyModes.getSupportedModes();
        modeIndex = (modeIndex + 1) % supportedModes.size();
        tag.setInteger(MODE_INDEX, modeIndex);
        playerIn.sendStatusMessage(new TextComponentTranslation("message.item.mode.changed", supportedModes.get(modeIndex).name()), true);

        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }

        Boolean access = ASSEMBLY_ACCESS_TOKEN.getIfPresent(player);

        if (Boolean.FALSE.equals(access)) {
            player.sendMessage(new TextComponentTranslation("message.assembly.tip.too_quickly"));
            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        ASSEMBLY_ACCESS_TOKEN.put(player, false);

        TileEntity tileEntity = worldIn.getTileEntity(pos);

        if (!(tileEntity instanceof TileMultiblockMachineController controller)) {
            return EnumActionResult.PASS;
        }

        Block block = worldIn.getBlockState(pos).getBlock();
        DynamicMachine machine = controller.getBlueprintMachine();
        if (machine == null) {
            if (block instanceof BlockController) {
                machine = ((BlockController) block).getParentMachine();
            }
            if (block instanceof BlockFactoryController) {
                machine = ((BlockFactoryController) block).getParentMachine();
            }
        }

        if (machine == null) {
            player.sendMessage(new TextComponentTranslation("message.assembly.missing.machine"));
            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }

        if (MachineAssemblyManager.machineExists(pos)) {
            player.sendMessage(new TextComponentTranslation("message.assembly.tip.already_assembly"));
            return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }

        EnumFacing controllerFacing = player.world.getBlockState(pos).getValue(BlockController.FACING);
        BlockArray machinePattern = new BlockArray(BlockArrayCache.getBlockArrayCache(machine.getPattern(), controllerFacing));

        int dynamicPatternSize = 0;
        Map<String, DynamicPattern> dynamicPatterns = machine.getDynamicPatterns();
        for (final DynamicPattern pattern : dynamicPatterns.values()) {
            dynamicPatternSize = Math.max(dynamicPatternSize, pattern.getMinSize());
        }

        for (final DynamicPattern pattern : dynamicPatterns.values()) {
            pattern.addPatternToBlockArray(
                    machinePattern,
                    Math.min(Math.max(pattern.getMinSize(), dynamicPatternSize), pattern.getMaxSize()),
                    pattern.getFaces().iterator().next(),
                    controllerFacing);
        }

        IMachineAssembly machineAssembly = getAssembly(worldIn, pos, player, machinePattern);

        MachineAssemblyManager.addMachineAssembly(machineAssembly);

        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public boolean isDamaged(final ItemStack stack) {
        return false;
    }

    @Override
    public void setDamage(final ItemStack stack, final int damage) {

    }

    abstract IMachineAssembly getAssembly(World world, BlockPos controllerPos, EntityPlayer player, BlockArray machineDef);
}
