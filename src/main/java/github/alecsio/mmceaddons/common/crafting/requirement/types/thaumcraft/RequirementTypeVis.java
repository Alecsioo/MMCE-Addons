package github.alecsio.mmceaddons.common.crafting.requirement.types.thaumcraft;

import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.component.base.RequiresMod;
import github.alecsio.mmceaddons.common.crafting.requirement.thaumcraft.RequirementVis;
import github.alecsio.mmceaddons.common.crafting.requirement.types.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.crafting.requirement.types.base.BaseRequirementType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Vis;
import github.alecsio.mmceaddons.common.tile.thaumcraft.TileVisProvider;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;

@RequiresMod(Mods.THAUMCRAFT_ID)
public class RequirementTypeVis extends BaseRequirementType<Vis, RequirementVis> {

    @Override
    public ComponentRequirement<Vis, ? extends RequirementType<Vis, RequirementVis>> createRequirement(IOType type, JsonObject jsonObject) {
        float amount = RequirementUtils.getRequiredFloat(jsonObject, "amount", ModularMachineryAddonsRequirements.KEY_REQUIREMENT_VIS.toString());
        int chunkRange = RequirementUtils.getOptionalInt(jsonObject, "chunkRange", 0); // Only the chunk the machine is in
        float minPerChunk = RequirementUtils.getOptionalFloat(jsonObject, "minPerChunk", 0);
        float maxPerChunk = RequirementUtils.getOptionalFloat(jsonObject, "maxPerChunk", TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK);

        return RequirementVis.from(type, chunkRange, amount, minPerChunk, maxPerChunk);
    }
}
