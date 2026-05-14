package github.alecsio.mmceaddons.common.integration.jei.component;

import github.alecsio.mmceaddons.common.hatch.nuclearcraft.IRequirementRadiation;
import github.alecsio.mmceaddons.common.integration.jei.component.base.JEIComponentBase;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Radiation;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.LayoutRadiation;
import hellfirepvp.modularmachinery.common.integration.recipe.RecipeLayoutPart;

import java.awt.*;

public class JEIComponentRadiation extends JEIComponentBase<Radiation> {

    public JEIComponentRadiation(IRequirementRadiation requirementRadiation, boolean scrubber) {
        super(new Radiation(requirementRadiation.getAmount(), requirementRadiation.getChunkRange(), scrubber), Radiation.class);
    }

    @Override
    public RecipeLayoutPart<Radiation> getLayoutPart(Point point) {
        return new LayoutRadiation(point);
    }

}
