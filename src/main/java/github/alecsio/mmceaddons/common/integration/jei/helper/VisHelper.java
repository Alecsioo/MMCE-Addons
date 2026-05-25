package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Vis;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class VisHelper extends BaseIngredientHelper<Vis> {

    @Override
    public String getUniqueId(Vis vis) {
        return "Vis";
    }

    @Override
    public String getResourceId(Vis vis) {
        return getUniqueId(vis);
    }

    @Override
    public Vis copyIngredient(Vis vis) {
        return new Vis(vis.getAmount(), vis.getChunkRange(), vis.getMinPerChunk(), vis.getMaxPerChunk());
    }
}
