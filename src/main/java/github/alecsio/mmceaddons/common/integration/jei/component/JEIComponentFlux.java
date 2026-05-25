package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Flux;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutFlux;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentFlux extends JEIComponentBase<Flux> {

    public JEIComponentFlux(Flux requirement) {
        super(requirement, Flux.class);
    }

    @Override
    public RecipeLayoutPart<Flux> getLayoutPart(Point offset) {
        return new LayoutFlux(offset, Flux.class);
    }
}
