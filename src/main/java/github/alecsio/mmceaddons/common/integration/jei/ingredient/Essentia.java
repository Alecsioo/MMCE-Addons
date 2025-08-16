package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.api.EssentiaStack;

import javax.annotation.Nonnull;
import java.util.List;

public class Essentia extends EssentiaStack implements IRequiresEquals<Essentia>, IIngredientType<Essentia>, ITooltippable {

    private static final Aspect DEFAULT_ASPECT = Aspect.AIR;
    private static final int DEFAULT_AMOUNT = 0;

    public Essentia() {
        super(DEFAULT_ASPECT, DEFAULT_AMOUNT);
    }

    public Essentia(Aspect aspect, int amount) {
        super(aspect, amount);
    }

    @Override
    public boolean equalsTo(Essentia other) {
        return getAmount() == other.getAmount() && getAspect().getTag().equals(other.getAspect().getTag());
    }

    @Override
    @Nonnull
    public Class<? extends Essentia> getIngredientClass() {
        return this.getClass();
    }

    @Override
    public List<String> getTooltip() {
        List<String> tooltip = Lists.newArrayList();
        tooltip.add(FormatUtils.format(I18n.format(LocalizationKeys.ASPECT), super.getAspect().getName()));
        tooltip.add(FormatUtils.format(I18n.format(LocalizationKeys.AMOUNT), String.valueOf(super.getAmount())));
        return tooltip;
    }
}
