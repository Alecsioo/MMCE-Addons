package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class PotentialEnergyHelper extends BaseIngredientHelper<PotentialEnergy> {

    @Override
    public String getUniqueId(PotentialEnergy potentialEnergy) {
        return getDisplayName(potentialEnergy);
    }

    @Override
    public String getResourceId(PotentialEnergy potentialEnergy) {
        return getUniqueId(potentialEnergy);
    }

    @Override
    public PotentialEnergy copyIngredient(PotentialEnergy potentialEnergy) {
        return new PotentialEnergy(potentialEnergy.getEnergy());
    }
}
