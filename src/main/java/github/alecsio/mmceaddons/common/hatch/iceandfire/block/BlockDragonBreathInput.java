package github.alecsio.mmceaddons.common.hatch.iceandfire.block;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import github.alecsio.mmceaddons.common.hatch.iceandfire.DragonType;
import github.alecsio.mmceaddons.common.hatch.iceandfire.TileDragonBreathProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
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
import java.util.HashMap;
import java.util.Map;

public class BlockDragonBreathInput extends BaseBlockMachineComponent {

    private static final Map<DragonType, String> TYPE_TO_KEY;

    static {
        TYPE_TO_KEY = new HashMap<>();

        TYPE_TO_KEY.put(DragonType.FIRE, "dragon.type.fire");
        TYPE_TO_KEY.put(DragonType.ICE, "dragon.type.ice");
        TYPE_TO_KEY.put(DragonType.LIGHTNING, "dragon.type.lightning");
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
        return new TileDragonBreathProvider();
    }
}
