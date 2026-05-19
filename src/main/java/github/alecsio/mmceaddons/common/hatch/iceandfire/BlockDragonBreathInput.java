package github.alecsio.mmceaddons.common.hatch.iceandfire;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import hellfirepvp.modularmachinery.common.block.BlockVariants;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockDragonBreathInput extends BaseBlockMachineComponent implements BlockVariants {

    public static final PropertyEnum<DragonType> DRAGON_TYPE = PropertyEnum.create("dragon_type", DragonType.class);
    private static final Map<DragonType, String> TYPE_TO_KEY;

    static {
        TYPE_TO_KEY = new HashMap<>();

        TYPE_TO_KEY.put(DragonType.FIRE, "dragon.type.fire");
        TYPE_TO_KEY.put(DragonType.ICE, "dragon.type.ice");
        TYPE_TO_KEY.put(DragonType.LIGHTNING, "dragon.type.lightning");
        TYPE_TO_KEY.put(DragonType.EMPTY, "dragon.type.empty");
    }

    public BlockDragonBreathInput() {
        super();
        this.setDefaultState(this.blockState.getBaseState().withProperty(DRAGON_TYPE, DragonType.EMPTY));
    }



    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        TileEntity te = worldIn.getTileEntity(pos);
        if (te instanceof TileDragonBreathProvider breathProvider) {
            ItemStack stack = playerIn.getHeldItem(hand);
            DragonType type = breathProvider.onRightClicked(stack);
            if (worldIn.isRemote) {
                ITextComponent textComponent = new TextComponentTranslation("message.modularmachineryaddons.dragonbreathhatch.unlocked");
                if (type != null) {
                    textComponent = new TextComponentTranslation("message.modularmachineryaddons.dragonbreathhatch.locked", I18n.format(TYPE_TO_KEY.get(type)
                    ));
                }
                playerIn.sendMessage(textComponent);
            }
        }

        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileDragonBreathProvider(state.getValue(DRAGON_TYPE));
    }

    @Override
    public Iterable<IBlockState> getValidStates() {
        List<IBlockState> states = new ArrayList<>();

        for (DragonType type : DragonType.values()) {
            states.add(this.getDefaultState().withProperty(DRAGON_TYPE, type));
        }

        return states;
    }

    @Override
    public String getBlockStateName(IBlockState iBlockState) {
        return iBlockState.getValue(DRAGON_TYPE).getName();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(DRAGON_TYPE, DragonType.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(DRAGON_TYPE).ordinal();
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, DRAGON_TYPE);
    }

}
