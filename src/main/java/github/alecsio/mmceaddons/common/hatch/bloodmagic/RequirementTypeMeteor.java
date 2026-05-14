package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Meteor;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.BLOODMAGIC_ID)
public class RequirementTypeMeteor extends BaseRequirementType<Meteor, RequirementMeteor> {

    @Override
    public ComponentRequirement<Meteor, ? extends RequirementType<Meteor, RequirementMeteor>> createRequirement(IOType type, JsonObject jsonObject) {
        String itemString = RequirementUtils.getRequiredString(jsonObject, "item", RequirementMeteor.class.getSimpleName());

        return RequirementMeteor.from(itemString);
    }
}
