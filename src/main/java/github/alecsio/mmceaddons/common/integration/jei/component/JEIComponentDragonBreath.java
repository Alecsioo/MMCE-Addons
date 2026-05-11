package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutDragonBreath;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentDragonBreath extends JEIComponentBase<DragonBreath> {

    public JEIComponentDragonBreath(DragonBreath requirement) {
        super(requirement, DragonBreath.class);
    }

    @Override
    public RecipeLayoutPart<DragonBreath> getLayoutPart(Point point) {
        return new LayoutDragonBreath(point);
    }
}
