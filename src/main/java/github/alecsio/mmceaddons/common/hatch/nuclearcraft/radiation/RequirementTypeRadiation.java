package github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Radiation;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.NUCLEARCRAFT_ID)
public class RequirementTypeRadiation extends BaseRequirementType<Radiation, RequirementRadiation> {

    @Override
    public ComponentRequirement<Radiation, ? extends RequirementType<Radiation, RequirementRadiation>> createRequirement(IOType type, JsonObject jsonObject) {
        double amount = RequirementUtils.getRequiredDouble(jsonObject, "amount", ModularMachineryAddonsRequirements.KEY_REQUIREMENT_RADIATION.toString());
        int chunkRange = RequirementUtils.getOptionalInt(jsonObject, "chunkRange", 0); // Only the chunk the machine is in

        return RequirementRadiation.from(type, chunkRange, amount);
    }
}
