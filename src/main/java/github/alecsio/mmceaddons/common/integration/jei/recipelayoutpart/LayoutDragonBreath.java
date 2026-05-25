package github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart;

import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.base.BaseRecipeLayoutPart;
import github.alecsio.mmceaddons.common.integration.jei.render.DragonBreathRenderer;
import mezz.jei.api.ingredients.IIngredientRenderer;

import java.awt.*;

public class LayoutDragonBreath extends BaseRecipeLayoutPart<DragonBreath> {

    public LayoutDragonBreath(Point offset) {
        super(offset, DragonBreath.class);
    }

    @Override
    public IIngredientRenderer<DragonBreath> provideIngredientRenderer() {
        return new DragonBreathRenderer();
    }
}
