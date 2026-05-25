package github.alecsio.mmceaddons.common.hatch.iceandfire;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.RequiresMod;
import github.alecsio.mmceaddons.common.hatch.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.ICE_AND_FIRE_ID)
public class RequirementTypeDragonBreath extends BaseRequirementType<DragonBreath, RequirementDragonBreath> {

    @Override
    public ComponentRequirement<DragonBreath, RequirementTypeDragonBreath> createRequirement(IOType ioType, JsonObject jsonObject) {
        return RequirementDragonBreath.from(ioType, RequirementUtils.getOptionalString(jsonObject, "dragon-type"), RequirementUtils.getOptionalInt(jsonObject, "amount", -1));
    }

}
