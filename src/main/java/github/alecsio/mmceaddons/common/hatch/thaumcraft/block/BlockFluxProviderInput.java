package github.alecsio.mmceaddons.common.hatch.thaumcraft.block;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.TileFluxProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFluxProviderInput extends BaseBlockMachineComponent {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileFluxProvider.Input();
    }
}
