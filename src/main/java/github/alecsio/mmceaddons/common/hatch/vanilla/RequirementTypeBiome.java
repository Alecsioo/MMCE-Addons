package github.alecsio.mmceaddons.common.hatch.vanilla;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Biome;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

public class RequirementTypeBiome extends BaseRequirementType<Biome, RequirementBiome> {

    @Override
    public ComponentRequirement<Biome, ? extends RequirementType<Biome, RequirementBiome>> createRequirement(IOType type, JsonObject jsonObject) {
        return RequirementBiome.from(type, RequirementUtils.getOptionalString(jsonObject, "biomeRegistryName"));
    }
}
