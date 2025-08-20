package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Vis;

import javax.annotation.Nonnull;

public class VisHelper extends BaseIngredientHelper<Vis> {

    @Override
    @Nonnull
    public String getUniqueId(@Nonnull Vis vis) {
        return "Vis";
    }

    @Override
    @Nonnull
    public String getResourceId(@Nonnull Vis vis) {
        return getUniqueId(vis);
    }

    @Override
    @Nonnull
    public Vis copyIngredient(@Nonnull Vis vis) {
        return new Vis(vis.getAmount(), vis.getChunkRange(), vis.getMinPerChunk(), vis.getMaxPerChunk());
    }
}
