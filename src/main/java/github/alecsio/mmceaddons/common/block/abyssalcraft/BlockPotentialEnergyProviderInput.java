package github.alecsio.mmceaddons.common.block.abyssalcraft;

import github.alecsio.mmceaddons.common.block.base.BaseBlockMachineComponent;
import github.alecsio.mmceaddons.common.tile.abyssalcraft.TilePotentialEnergyProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;


@SuppressWarnings("deprecation")
public class BlockPotentialEnergyProviderInput extends BaseBlockMachineComponent {

    // Required to avoid hacky mixins on abyssal's side (only non-full blocks can accept PE)
    @Override
    public boolean isFullCube(@Nonnull IBlockState state) {
        return false;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state) {
        return new TilePotentialEnergyProvider.Input();
    }
}
