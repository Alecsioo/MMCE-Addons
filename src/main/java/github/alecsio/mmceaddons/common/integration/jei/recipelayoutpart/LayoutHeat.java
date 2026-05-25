package github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart;

import github.alecsio.mmceaddons.common.integration.jei.ingredient.Heat;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.base.BaseRecipeLayoutPart;

import java.awt.*;

public class LayoutHeat extends BaseRecipeLayoutPart<Heat> {

    public LayoutHeat(Point offset) {
        super(offset, Heat.class);
    }
}
