package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;

import javax.annotation.Nonnull;

public class PotentialEnergyHelper extends BaseIngredientHelper<PotentialEnergy> {

    @Override
    @Nonnull
    public String getUniqueId(@Nonnull PotentialEnergy potentialEnergy) {
        return getDisplayName(potentialEnergy);
    }

    @Override
    @Nonnull
    public String getResourceId(@Nonnull PotentialEnergy potentialEnergy) {
        return getUniqueId(potentialEnergy);
    }

    @Override
    @Nonnull
    public PotentialEnergy copyIngredient(@Nonnull PotentialEnergy potentialEnergy) {
        return new PotentialEnergy(potentialEnergy.getEnergy());
    }
}
