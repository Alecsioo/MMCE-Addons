package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutPotentialEnergy;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentPotentialEnergy extends JEIComponentBase<PotentialEnergy> {

    public JEIComponentPotentialEnergy(PotentialEnergy requirement) {
        super(requirement, PotentialEnergy.class);
    }

    @Override
    public RecipeLayoutPart<PotentialEnergy> getLayoutPart(Point point) {
        return new LayoutPotentialEnergy(point);
    }
}
