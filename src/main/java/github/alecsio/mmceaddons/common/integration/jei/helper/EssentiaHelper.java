package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Essentia;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class EssentiaHelper extends BaseIngredientHelper<Essentia> {

    @Override
    public String getUniqueId(Essentia essentiaStack) {
        return essentiaStack.getAspect().getTag();
    }

    @Override
    public String getResourceId(Essentia t) {
        return getUniqueId(t);
    }

    @Override
    public Essentia copyIngredient(Essentia essentiaStack) {
        return new Essentia(essentiaStack.getAspect(), essentiaStack.getAmount());
    }
}
