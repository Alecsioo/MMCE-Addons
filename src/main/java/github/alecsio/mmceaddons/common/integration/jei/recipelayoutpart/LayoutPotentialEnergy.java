package github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart;

import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.base.BaseRecipeLayoutPart;
import github.alecsio.mmceaddons.common.integration.jei.render.PotentialEnergyRenderer;
import mezz.jei.api.ingredients.IIngredientRenderer;

import java.awt.*;

public class LayoutPotentialEnergy extends BaseRecipeLayoutPart<PotentialEnergy> {

    public LayoutPotentialEnergy(Point offset) {
        super(offset, PotentialEnergy.class);
    }

    @Override
    public IIngredientRenderer<PotentialEnergy> provideIngredientRenderer() {
        return new PotentialEnergyRenderer();
    }
}
