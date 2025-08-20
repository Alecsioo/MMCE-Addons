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
    private float minPerChunk;
    private float maxPerChunk;

    public Vis() {}

    public Vis(float amount, int chunkRange, float minPerChunk, float maxPerChunk) {
        this.amount = amount;
        this.chunkRange = chunkRange;
        this.minPerChunk = minPerChunk;
        this.maxPerChunk = maxPerChunk;
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

    public float getMinPerChunk() {
        return minPerChunk;
    }

    public float getMaxPerChunk() {
        return maxPerChunk;
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
