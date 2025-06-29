package github.alecsio.mmceaddons.common.item;

import github.alecsio.mmceaddons.common.assembly.AdvancedMachineDisassembly;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import hellfirepvp.modularmachinery.common.util.BlockArray;
import ink.ikx.mmce.common.utils.StructureIngredient;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockAir;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemAdvancedMachineDisassembler extends BaseItemAdvancedMachineBuilder {

    @Override
    IMachineAssembly getAssembly(World world, BlockPos controllerPos, EntityPlayer player, BlockArray machineDef) {
        return new AdvancedMachineDisassembly(world, controllerPos, player, new StructureIngredient(getBlockStateIngredientList(world, controllerPos, machineDef), null));
    }

    public List<StructureIngredient.ItemIngredient> getBlockStateIngredientList(World world, BlockPos ctrlPos, BlockArray machineDef) {
        List<StructureIngredient.ItemIngredient> ingredientList = new ArrayList<>();
        machineDef.getPattern().forEach((pos, info) -> {
            BlockPos realPos = ctrlPos.add(pos);
            if (info.matches(world, realPos, false)) {
                ingredientList.add(new StructureIngredient.ItemIngredient(pos, info.getBlockStateIngredientList(), info.getMatchingTag()));
            }
        });
        return ingredientList;
    }
}
