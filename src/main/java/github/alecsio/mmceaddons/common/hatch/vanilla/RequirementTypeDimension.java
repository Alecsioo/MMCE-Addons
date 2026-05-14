package github.alecsio.mmceaddons.common.hatch.vanilla;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Dimension;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

public class RequirementTypeDimension extends BaseRequirementType<Dimension, RequirementDimension> {

    @Override
    public ComponentRequirement<Dimension, ? extends RequirementType<Dimension, RequirementDimension>> createRequirement(IOType type, JsonObject jsonObject) {
        return RequirementDimension.from(type, RequirementUtils.getRequiredInt(jsonObject, "id", "id"));
    }
}
