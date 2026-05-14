package github.alecsio.mmceaddons.common.hatch.nuclearcraft.block;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.TileRadiationProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockRadiationProviderOutput extends BaseBlockMachineComponent {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileRadiationProvider.Output();
    }
}
