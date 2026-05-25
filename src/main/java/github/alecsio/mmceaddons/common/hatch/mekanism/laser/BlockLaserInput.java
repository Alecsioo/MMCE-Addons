package github.alecsio.mmceaddons.common.hatch.mekanism.laser;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockLaserInput  extends BaseBlockMachineComponent {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileLaserProvider();
    }
}
