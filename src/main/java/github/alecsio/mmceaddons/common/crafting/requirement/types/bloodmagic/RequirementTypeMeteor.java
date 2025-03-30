package github.alecsio.mmceaddons.common.crafting.requirement.types.bloodmagic;

import WayofTime.bloodmagic.meteor.MeteorRegistry;
import com.google.gson.JsonObject;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.requirement.bloodmagic.RequirementMeteor;
import github.alecsio.mmceaddons.common.exception.ConsistencyException;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Meteor;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import kport.modularmagic.common.utils.RequirementUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;

public class RequirementTypeMeteor extends RequirementType<github.alecsio.mmceaddons.common.integration.jei.ingredient.Meteor, RequirementMeteor> {
    @Override
    public ComponentRequirement<Meteor, ? extends RequirementType<Meteor, RequirementMeteor>> createRequirement(IOType type, JsonObject jsonObject) {
        String itemString = RequirementUtils.getRequiredString(jsonObject, "item", RequirementMeteor.class.getSimpleName());
        Item item = Item.getByNameOrId(itemString);
        if (item == null) {throw new ConsistencyException(String.format("Failed to load recipe for %s. Item %s doesn't exist!", this.getClass().getSimpleName(), itemString));}

        int amount = RequirementUtils.getRequiredInt(jsonObject, "amount", "amount");
        ItemStack itemStack = new ItemStack(item, amount);
        var meteor = MeteorRegistry.getMeteorForItem(itemStack);

        if (meteor == null) {throw new ConsistencyException(String.format("Failed to find meteor with catalyst: %s", itemStack));}

        return new RequirementMeteor(type, new Meteor(meteor.getCatalystStack(), meteor.getComponents(), meteor.getExplosionStrength(), meteor.getRadius()));
    }

    @Nullable
    @Override
    public String requiresModid() {
        return Mods.BLOODMAGIC.modid;
    }
}
