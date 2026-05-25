package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Laser;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class LaserHelper extends BaseIngredientHelper<Laser>  {

    @Override
    public String getUniqueId(Laser laser) {
        return getDisplayName(laser);
    }

    @Override
    public String getResourceId(Laser laser) {
        return getUniqueId(laser);
    }

    @Override
    public Laser copyIngredient(Laser laser) {
        return new Laser(laser.power());
    }
}
