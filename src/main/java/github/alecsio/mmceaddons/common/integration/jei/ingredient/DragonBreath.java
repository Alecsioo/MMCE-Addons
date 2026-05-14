package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import github.alecsio.mmceaddons.common.hatch.iceandfire.DragonType;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;

import java.util.Collections;
import java.util.List;

public class DragonBreath implements IRequiresEquals<DragonBreath>, IIngredientType<DragonBreath>, ITooltippable {

    private DragonType dragonType;
    private int amount;

    public DragonBreath(DragonType dragonType, int amount) {
        this.dragonType = dragonType;
        this.amount = amount;
    }

    public DragonBreath() {}

    public DragonType getDragonType() {
        return dragonType;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equalsTo(DragonBreath other) {
        return this.dragonType == other.dragonType && this.amount == other.amount;
    }

    @Override
    public List<String> getTooltip() {
        return Collections.emptyList();
    }

    @Override
    public Class<? extends DragonBreath> getIngredientClass() {
        return this.getClass();
    }
}
