package github.alecsio.mmceaddons.common.hatch.thaumcraft.vis;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockVisProviderOutput extends BaseBlockMachineComponent {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileVisProvider.Output();
    }
}
