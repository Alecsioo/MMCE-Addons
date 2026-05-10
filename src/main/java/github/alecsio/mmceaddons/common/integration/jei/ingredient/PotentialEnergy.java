package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;

import javax.annotation.Nonnull;
import java.util.List;

public class PotentialEnergy implements IRequiresEquals<PotentialEnergy>, IIngredientType<PotentialEnergy>, ITooltippable {

    private final float energy;

    public PotentialEnergy() {
        this(0.0f);
    }

    public PotentialEnergy(float energy) {
        this.energy = energy;
    }

    public float getEnergy() {
        return energy;
    }

    @Override
    @Nonnull
    public Class<? extends PotentialEnergy> getIngredientClass() {
        return this.getClass();
    }

    @Override
    public boolean equalsTo(PotentialEnergy other) {
        return this.energy == other.energy;
    }

    @Override
    public List<String> getTooltip() {
        return null;
    }
}
