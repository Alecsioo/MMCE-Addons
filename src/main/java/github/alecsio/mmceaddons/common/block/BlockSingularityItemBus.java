package github.alecsio.mmceaddons.common.block;

import github.alecsio.mmceaddons.CommonProxy;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import hellfirepvp.modularmachinery.common.block.BlockBus;
import hellfirepvp.modularmachinery.common.tiles.base.TileItemBus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

abstract public class BlockSingularityItemBus extends BlockBus {
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            TileEntity te = worldIn.getTileEntity(pos);
            if (te instanceof TileItemBus) {
                playerIn.openGui(ModularMachineryAddons.MODID, CommonProxy.GuiType.VACUUM_INVENTORY.ordinal(), worldIn, pos.getX(), pos.getY(), pos.getZ());
            }
        }

        return true;
    }
}
