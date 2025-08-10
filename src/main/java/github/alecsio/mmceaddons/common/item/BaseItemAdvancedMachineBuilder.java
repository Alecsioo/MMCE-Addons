package github.alecsio.mmceaddons.common.item;

import appeng.api.features.INetworkEncodable;
import appeng.util.Platform;
import github.alecsio.mmceaddons.CommonProxy;
import github.alecsio.mmceaddons.client.MouseScrollHandler;
import github.alecsio.mmceaddons.common.assembly.AssemblyModes;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.MachineAssemblyManager;
import github.alecsio.mmceaddons.common.mixin.BlockStateDescriptorMixin;
import github.alecsio.mmceaddons.common.mixin.IBlockArrayInvoker;
import github.alecsio.mmceaddons.common.mixin.IBlockInformationAccessor;
import github.kasuminova.mmce.common.util.DynamicPattern;
import hellfirepvp.modularmachinery.common.block.BlockController;
import hellfirepvp.modularmachinery.common.block.BlockFactoryController;
import hellfirepvp.modularmachinery.common.machine.DynamicMachine;
import hellfirepvp.modularmachinery.common.modifier.SingleBlockModifierReplacement;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import hellfirepvp.modularmachinery.common.util.BlockArrayCache;
import hellfirepvp.modularmachinery.common.util.IBlockStateDescriptor;
import ink.ikx.mmce.common.utils.FluidUtils;
import ink.ikx.mmce.common.utils.StructureIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.function.Function;

import static github.alecsio.mmceaddons.common.assembly.handler.MachineAssemblyEventHandler.ASSEMBLY_ACCESS_TOKEN;

@Optional.Interface(modid = "appliedenergistics2", iface = "appeng.api.features.INetworkEncodable")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class BaseItemAdvancedMachineBuilder extends Item implements INetworkEncodable {

    public static final String AE2_ENCRYPTION_KEY = "encryptionKey";
    public static final String MODE_INDEX = "modeIndex";

    public BaseItemAdvancedMachineBuilder() {
        this.setMaxStackSize(1);
        this.setNoRepair();

        setCreativeTab(CommonProxy.creativeTabModularMachineryAddons);
    }

    public boolean onControllerRightClick(EntityPlayer player, BlockPos controllerPos, World world) {
        Boolean access = ASSEMBLY_ACCESS_TOKEN.getIfPresent(player);

        if (Boolean.FALSE.equals(access)) {
            player.sendMessage(new TextComponentTranslation("message.assembly.too.quickly"));
            return false;
        }
        ASSEMBLY_ACCESS_TOKEN.put(player, false);

        TileEntity tileEntity = world.getTileEntity(controllerPos);

        if (!(tileEntity instanceof TileMultiblockMachineController controller)) {
            return false;
        }

        Block block = world.getBlockState(controllerPos).getBlock();
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
            return false;
        }

        if (MachineAssemblyManager.machineExists(controllerPos)) {
            player.sendMessage(new TextComponentTranslation("message.assembly.tip.already_assembly"));
            return false;
        }

        EnumFacing controllerFacing = player.world.getBlockState(controllerPos).getValue(BlockController.FACING);
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

        // I hate this :D
        var modifiers = machine.getModifiers();
        BlockArray machinePatternCopy = new BlockArray();
        Map<BlockPos, BlockArray.BlockInformation> blockPosBlockInformationMapCopy = getPatternDeepCopy(machinePattern.getPattern());
        for (BlockPos blockPos : blockPosBlockInformationMapCopy.keySet()) {
            machinePatternCopy.getPattern().put(blockPos, blockPosBlockInformationMapCopy.get(blockPos));
            ((IBlockArrayInvoker) machinePatternCopy).invokeUpdateSize(blockPos);
        }

        for (final BlockPos modifierPos : modifiers.keySet()) {
            List<SingleBlockModifierReplacement> modifierReplacements = modifiers.get(modifierPos);
            BlockArray.BlockInformation blockInfo = machinePatternCopy.getPattern().get(modifierPos.rotate(getRotationFrom(controllerFacing)));
            for (final SingleBlockModifierReplacement modifierReplacement : modifierReplacements) {
                BlockArray.BlockInformation modifierBlockInfo = modifierReplacement.getBlockInformation();
                blockInfo.addMatchingStates(deepCopyDescriptors(((IBlockInformationAccessor) modifierBlockInfo).getMatchingStates()));
            }
        }

        IMachineAssembly machineAssembly = getAssembly(world, controllerPos, player, machinePatternCopy);

        MachineAssemblyManager.addMachineAssembly(machineAssembly);
        return true;
    }

    public List<StructureIngredient.ItemIngredient> getBlockStateIngredientList(World world, BlockPos ctrlPos, BlockArray machineDef) {
        List<StructureIngredient.ItemIngredient> ingredientList = new ArrayList<>();
        machineDef.getPattern().forEach((pos, info) -> {
            BlockPos realPos = ctrlPos.add(pos);
            IBlockState actualState = world.getBlockState(realPos);
            if (shouldProcessIngredient(actualState, info.getSampleState())) {
                ingredientList.add(new StructureIngredient.ItemIngredient(pos, info.getBlockStateIngredientList(), info.getMatchingTag()));
            }
        });
        return ingredientList;
    }

    protected List<StructureIngredient.FluidIngredient> getBlockStateFluidIngredientList(List<StructureIngredient.ItemIngredient> itemIngredients) {
        List<StructureIngredient.FluidIngredient> fluidIngredientList = new ArrayList<>();
        Iterator<StructureIngredient.ItemIngredient> iterator = itemIngredients.iterator();
        while (iterator.hasNext()) {
            StructureIngredient.ItemIngredient itemIngredient = iterator.next();
            BlockPos pos = itemIngredient.pos();
            List<Tuple<FluidStack, IBlockState>> fluidIngredient = new ArrayList<>();

            for (final Tuple<ItemStack, IBlockState> tuple : itemIngredient.ingredientList()) {
                IBlockState state = tuple.getSecond();
                FluidStack fluidStack = FluidUtils.getFluidStackFromBlockState(state);
                if (fluidStack == null) {
                    continue;
                }
                fluidIngredient.add(new Tuple<>(fluidStack, state));
            }

            if (!fluidIngredient.isEmpty()) {
                fluidIngredientList.add(new StructureIngredient.FluidIngredient(pos, fluidIngredient));
                iterator.remove();
            }
        }
        return fluidIngredientList;
    }

    protected Map<BlockPos, BlockArray.BlockInformation> getPatternDeepCopy(Map<BlockPos, BlockArray.BlockInformation> original) {
        Map<BlockPos, BlockArray.BlockInformation> copy = new HashMap<>();
        for (Map.Entry<BlockPos, BlockArray.BlockInformation> entry : original.entrySet()) {
            BlockPos originalPos = entry.getKey();
            BlockArray.BlockInformation originalBlockInfo = entry.getValue();

            BlockPos copiedPos = new BlockPos(originalPos.getX(), originalPos.getY(), originalPos.getZ()).toImmutable();

            List<IBlockStateDescriptor> stateDescriptorsCopy = deepCopyDescriptors(((IBlockInformationAccessor) originalBlockInfo).getMatchingStates());

            BlockArray.BlockInformation copiedBlockInfo = new BlockArray.BlockInformation(stateDescriptorsCopy);

            copy.put(copiedPos, copiedBlockInfo);
        }
        return copy;
    }

    private List<IBlockStateDescriptor> deepCopyDescriptors(List<IBlockStateDescriptor> original) {
        return deepCopyList(original, stateDescriptor -> {
            List<IBlockState> applicableStatesCopy = deepCopyList(stateDescriptor.getApplicable(), state -> state);
            return BlockStateDescriptorMixin.invokeConstructor(applicableStatesCopy);
        });
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
            int modeIndex = Math.min(Math.abs(tag.getInteger(MODE_INDEX)), modes.size()); // Safeguard against inconsistent values, but it shouldn't happen
            tooltip.add(I18n.format("tooltip.builder." + modes.get(modeIndex).name().toLowerCase()));
        }
        tooltip.add(I18n.format("tooltip.builder.change.modes"));

        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    /**
     * Called from {@link MouseScrollHandler}
     */
    public static void onMouseScroll(ItemStack builderStack, EntityPlayer player, ScrollDirection direction) {
        NBTTagCompound tag = builderStack.getTagCompound();
        if (tag == null) {
            tag = new NBTTagCompound();
            builderStack.setTagCompound(tag);
        }

        List<AssemblyModes> supportedModes = AssemblyModes.getSupportedModes();
        int modeIndex = tag.getInteger(MODE_INDEX);
        modeIndex = (direction == ScrollDirection.UP
                ? (modeIndex + 1) % supportedModes.size()
                : (modeIndex - 1 + supportedModes.size()) % supportedModes.size());
        tag.setInteger(MODE_INDEX, modeIndex);
        player.sendStatusMessage(new TextComponentTranslation("tooltip.builder." + supportedModes.get(tag.getInteger(MODE_INDEX)).name().toLowerCase()), true);
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

    private static <T> List<T> deepCopyList(List<T> original, Function<T, T> copyFunction) {
        List<T> newList = new ArrayList<>(original.size());
        for (T t : original) {
            newList.add(deepCopy(t, copyFunction));
        }
        return newList;
    }

    private static <T> T deepCopy(T original, Function<T, T> copyFunction) {
        return copyFunction.apply(original);
    }

    private Rotation getRotationFrom(EnumFacing facing) {
        return switch (facing) {
            case NORTH -> Rotation.NONE;
            case EAST -> Rotation.CLOCKWISE_90;
            case SOUTH -> Rotation.CLOCKWISE_180;
            case WEST -> Rotation.COUNTERCLOCKWISE_90;
            default -> Rotation.NONE; // UP and DOWN don't map to horizontal rotation
        };
    }

    abstract IMachineAssembly getAssembly(World world, BlockPos controllerPos, EntityPlayer player, BlockArray machineDef);
    abstract boolean shouldProcessIngredient(IBlockState currentState, IBlockState expectedState);
}
