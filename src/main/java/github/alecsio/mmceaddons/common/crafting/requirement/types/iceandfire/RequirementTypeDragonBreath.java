package github.alecsio.mmceaddons.common.crafting.requirement.types.iceandfire;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.component.base.RequiresMod;
import github.alecsio.mmceaddons.common.crafting.requirement.iceandfire.RequirementDragonBreath;
import github.alecsio.mmceaddons.common.crafting.requirement.types.base.BaseRequirementType;
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
