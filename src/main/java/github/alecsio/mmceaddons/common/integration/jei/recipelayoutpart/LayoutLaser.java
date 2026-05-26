package github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart;

import github.alecsio.mmceaddons.common.integration.jei.ingredient.Laser;
import github.alecsio.mmceaddons.common.integration.jei.recipelayoutpart.base.BaseRecipeLayoutPart;

import java.awt.*;

public class LayoutLaser extends BaseRecipeLayoutPart<Laser> {

    public LayoutLaser(Point offset) {
        super(offset, Laser.class);
    }
}
