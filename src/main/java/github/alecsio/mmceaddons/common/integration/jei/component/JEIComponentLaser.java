package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Laser;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutLaser;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentLaser extends JEIComponentBase<Laser> {

    public JEIComponentLaser(Laser requirement) {
        super(requirement, Laser.class);
    }

    @Override
    public RecipeLayoutPart<Laser> getLayoutPart(Point offset) {
        return new LayoutLaser(offset);
    }
}
