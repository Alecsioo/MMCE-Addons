package github.alecsio.mmceaddons.common.crafting.requirement.types.abyssalcraft;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.component.base.RequiresMod;
import github.alecsio.mmceaddons.common.crafting.requirement.abyssalcraft.RequirementPotentialEnergy;
import github.alecsio.mmceaddons.common.crafting.requirement.types.base.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.ABYSSALCRAFT_ID)
public class RequirementTypePotentialEnergy extends BaseRequirementType<PotentialEnergy, RequirementPotentialEnergy> {
    @Override
    public ComponentRequirement<PotentialEnergy, ? extends RequirementType<PotentialEnergy, RequirementPotentialEnergy>> createRequirement(IOType ioType, JsonObject jsonObject) {
        return RequirementPotentialEnergy.from(ioType, RequirementUtils.getRequiredPositiveFloat(jsonObject, "potentialEnergy", "potentialEnergy"));
    }
}
