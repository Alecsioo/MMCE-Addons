package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Radiation;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class RadiationHelper extends BaseIngredientHelper<Radiation> {

    @Override
    public String getUniqueId(Radiation t) {
        return "Radiation";
    }

    @Override
    public String getResourceId(Radiation t) {
        return getUniqueId(t);
    }

    @Override
    public Radiation copyIngredient(Radiation t) {
        return new Radiation(t.getAmount(), t.getChunkRange(), t.isScrubber());
    }
}
