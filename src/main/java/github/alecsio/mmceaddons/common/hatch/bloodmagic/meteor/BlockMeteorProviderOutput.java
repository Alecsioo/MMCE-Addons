package github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockMeteorProviderOutput extends BaseBlockMachineComponent {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileMeteorProvider.Output();
    }
}
