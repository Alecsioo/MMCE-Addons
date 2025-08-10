package github.alecsio.mmceaddons.common.item;

import github.alecsio.mmceaddons.common.assembly.AdvancedMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import ink.ikx.mmce.common.utils.StructureIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemAdvancedMachineAssembler extends BaseItemAdvancedMachineBuilder {

    @Override
    IMachineAssembly getAssembly(World world, BlockPos controllerPos, EntityPlayer player, BlockArray machineDef) {
        List<StructureIngredient.ItemIngredient> itemIngredients = getBlockStateIngredientList(world, controllerPos, machineDef);
        List<StructureIngredient.FluidIngredient> fluidIngredients = getBlockStateFluidIngredientList(itemIngredients);
        return new AdvancedMachineAssembly(world, controllerPos, player, new StructureIngredient(itemIngredients, fluidIngredients));
    }

    @Override
    boolean shouldProcessIngredient(IBlockState currentState, IBlockState expectedState) {
        // Assemblies only process block positions where a block != the expected one is present
        return currentState.getBlock() != expectedState.getBlock();
    }

}
