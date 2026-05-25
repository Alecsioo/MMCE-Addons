package github.alecsio.mmceaddons.common.hatch.mekanism.heat;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Heat;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.MEKANISM_ID)
public class RequirementTypeHeat extends BaseRequirementType<Heat, RequirementHeat> {

    @Override
    public ComponentRequirement<Heat, ? extends RequirementType<Heat, RequirementHeat>> createRequirement(IOType type, JsonObject jsonObject) {
        return RequirementHeat.from(type, RequirementUtils.getRequiredDouble(jsonObject, "amount", "heat"));
    }
}
