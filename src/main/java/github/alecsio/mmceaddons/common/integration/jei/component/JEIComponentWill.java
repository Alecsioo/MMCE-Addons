package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.hatch.bloodmagic.RequirementWillMultiChunk;
import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;
import kport.modularmagic.common.integration.jei.ingredient.DemonWill;
import kport.modularmagic.common.integration.jei.recipelayoutpart.LayoutWill;

import java.awt.*;

public class JEIComponentWill extends JEIComponentBase<DemonWill> {

    public JEIComponentWill(RequirementWillMultiChunk requirement) {
        super(new DemonWill(requirement.willType, requirement.getAmount()), DemonWill.class);
    }

    @Override
    public RecipeLayoutPart<DemonWill> getLayoutPart(Point offset) {
        return new LayoutWill(offset);
    }

}
