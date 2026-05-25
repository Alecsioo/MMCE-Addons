package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Heat;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class HeatHelper extends BaseIngredientHelper<Heat> {

    @Override
    public String getUniqueId(Heat heat) {
        return getDisplayName(heat);
    }

    @Override
    public String getResourceId(Heat heat) {
        return getUniqueId(heat);
    }

    @Override
    public Heat copyIngredient(Heat heat) {
        return new Heat(heat.temperature());
    }
}
