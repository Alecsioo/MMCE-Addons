package github.alecsio.mmceaddons.common.crafting.requirement.types.nuclearcraft;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.component.base.RequiresMod;
import github.alecsio.mmceaddons.common.crafting.requirement.nuclearcraft.RequirementRadiation;
import github.alecsio.mmceaddons.common.crafting.requirement.types.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.crafting.requirement.types.base.BaseRequirementType;
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
