package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.hatch.thaumcraft.RequirementEssentia;
import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Essentia;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutEssentia;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentEssentia extends JEIComponentBase<Essentia> {

    public JEIComponentEssentia(RequirementEssentia requirement) {
        super(requirement.getEssentiaStack(), Essentia.class);
    }

    @Override
    public RecipeLayoutPart<Essentia> getLayoutPart(Point offset) {
        return new LayoutEssentia(offset);
    }
}
