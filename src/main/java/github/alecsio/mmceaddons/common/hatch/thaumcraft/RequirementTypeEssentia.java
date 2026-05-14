package github.alecsio.mmceaddons.common.hatch.thaumcraft;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Essentia;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.THAUMICENERGISTICS_ID)
public class RequirementTypeEssentia extends BaseRequirementType<Essentia, RequirementEssentia> {

    @Override
    public ComponentRequirement<Essentia, ? extends RequirementType<Essentia, RequirementEssentia>> createRequirement(IOType type, JsonObject recipe) {
        String aspect = RequirementUtils.getRequiredString(recipe, "aspect", "aspect");
        int amount = RequirementUtils.getRequiredInt(recipe, "amount", "amount");

        return RequirementEssentia.from(type, aspect, amount);
    }
}
