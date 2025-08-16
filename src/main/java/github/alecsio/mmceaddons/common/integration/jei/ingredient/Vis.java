package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;
import java.util.List;

public class Vis implements IRequiresEquals<Vis>, IIngredientType<Vis>, ITooltippable {

    private float amount;
    private int chunkRange;

    public Vis() {}

    public Vis(float amount, int chunkRange) {
        this.amount = amount;
        this.chunkRange = chunkRange;
    }

    @Override
    public boolean equalsTo(Vis other) {
        return this.amount == other.amount && this.chunkRange == other.chunkRange;
    }

    public float getAmount() {
        return amount;
    }

    public int getChunkRange() {
        return chunkRange;
    }

    @Override
    @Nonnull
    public Class<? extends Vis> getIngredientClass() {
        return this.getClass();
    }

    @Override
    public List<String> getTooltip() {
        List<String> tooltip = Lists.newArrayList();
        tooltip.add(FormatUtils.format(I18n.format(LocalizationKeys.VIS), String.valueOf(this.amount)));
        tooltip.add(FormatUtils.format(I18n.format(LocalizationKeys.CHUNK_RANGE), String.valueOf(this.chunkRange)));
        return tooltip;
    }
}
