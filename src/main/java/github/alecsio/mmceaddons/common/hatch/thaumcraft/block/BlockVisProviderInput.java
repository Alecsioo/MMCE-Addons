package github.alecsio.mmceaddons.common.hatch.thaumcraft.block;

import github.alecsio.mmceaddons.common.hatch.BaseBlockMachineComponent;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.TileVisProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockVisProviderInput extends BaseBlockMachineComponent {

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TileVisProvider.Input();
    }
}
