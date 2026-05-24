package github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Radiation;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.NUCLEARCRAFT_ID)
public class RequirementTypeScrubber extends BaseRequirementType<Radiation, RequirementScrubber> {

    @Override
    public ComponentRequirement<Radiation, ? extends RequirementType<Radiation, RequirementScrubber>> createRequirement(IOType ioType, JsonObject jsonObject) {
        if (ioType == IOType.OUTPUT) {throw new RequirementPrerequisiteFailedException("output requirement type is not supported");}

        int chunkRange = RequirementUtils.getOptionalInt(jsonObject, "chunkRange", 0); // Only the chunk the machine is in

        return RequirementScrubber.from(chunkRange);
    }
}
