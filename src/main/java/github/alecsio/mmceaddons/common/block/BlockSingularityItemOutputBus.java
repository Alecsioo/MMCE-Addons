package github.alecsio.mmceaddons.common.block;

import github.alecsio.mmceaddons.common.tile.TileSingularityItemOutputBus;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockSingularityItemOutputBus extends BlockSingularityItemBus {
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState iBlockState) {
        return new TileSingularityItemOutputBus(iBlockState.getValue(BUS_TYPE));
    }
}
