package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Flux;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FluxHelper extends BaseIngredientHelper<Flux> {

    @Override
    public String getUniqueId(Flux flux) {
        return getDisplayName(flux);
    }

    @Override
    public String getResourceId(Flux flux) {
        return getUniqueId(flux);
    }

    @Override
    public Flux copyIngredient(Flux flux) {
        return new Flux(flux.amount(), flux.chunkRange());
    }
}
