package github.alecsio.mmceaddons.common.hatch.mekanism.laser;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Laser;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.MEKANISM_ID)
public class RequirementTypeLaser extends BaseRequirementType<Laser, RequirementLaser> {
    @Override
    public ComponentRequirement<Laser, ? extends RequirementType<Laser, RequirementLaser>> createRequirement(IOType type, JsonObject jsonObject) {
        if (type == IOType.OUTPUT) {throw new RequirementPrerequisiteFailedException("output requirement type is not supported");}

        return RequirementLaser.from(IOType.INPUT, RequirementUtils.getRequiredPositiveDouble(jsonObject, "power", "laser"));
    }
}
