package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;

@Desugar
public record Flux(float amount, int chunkRange) implements IRequiresEquals<Flux>, IIngredientType<Flux>, ITooltippable {

    @Override
    public boolean equalsTo(Flux other) {
        return amount == other.amount && chunkRange == other.chunkRange;
    }

    @Override
    @Nonnull
    public Class<? extends Flux> getIngredientClass() {
        return this.getClass();
    }

    @Override
    public List<String> getTooltip() {
        List<String> tooltip = Lists.newArrayList();
        tooltip.add(FormatUtils.format(TextFormatting.DARK_PURPLE, I18n.format(LocalizationKeys.FLUX), String.valueOf(amount)));
        tooltip.add(FormatUtils.format(TextFormatting.DARK_PURPLE, I18n.format(LocalizationKeys.CHUNK_RANGE), String.valueOf(chunkRange)));
        return tooltip;
    }
}
