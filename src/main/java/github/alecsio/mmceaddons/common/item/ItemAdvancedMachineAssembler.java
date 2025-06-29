package github.alecsio.mmceaddons.common.item;

import github.alecsio.mmceaddons.common.assembly.AdvancedMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import ink.ikx.mmce.common.utils.StructureIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemAdvancedMachineAssembler extends BaseItemAdvancedMachineBuilder {

    @Override
    IMachineAssembly getAssembly(World world, BlockPos controllerPos, EntityPlayer player, BlockArray machineDef) {
        return new AdvancedMachineAssembly(world, controllerPos, player, StructureIngredient.of(world, controllerPos, machineDef));
    }

}
