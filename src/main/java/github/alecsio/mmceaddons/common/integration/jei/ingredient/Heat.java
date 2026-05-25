package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mcp.MethodsReturnNonnullByDefault;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Desugar
public record Heat(double temperature) implements IRequiresEquals<Heat>, IIngredientType<Heat>, ITooltippable {

    @Override
    public boolean equalsTo(Heat other) {
        return this.temperature == other.temperature;
    }

    @Override
    public List<String> getTooltip() {
        return Lists.newArrayList(FormatUtils.format(I18n.format("jei.tooltip.modularmachineryaddons.heat"), MekanismUtils.getTemperatureDisplay(temperature, UnitDisplayUtils.TemperatureUnit.CELSIUS)));
    }

    @Override
    public Class<? extends Heat> getIngredientClass() {
        return this.getClass();
    }
}
