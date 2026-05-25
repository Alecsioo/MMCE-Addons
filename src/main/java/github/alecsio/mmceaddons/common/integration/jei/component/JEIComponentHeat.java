package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Heat;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutHeat;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentHeat extends JEIComponentBase<Heat> {

    public JEIComponentHeat(Heat requirement) {
        super(requirement, Heat.class);
    }

    @Override
    public RecipeLayoutPart<Heat> getLayoutPart(Point offset) {
        return new LayoutHeat(offset);
    }
}
