package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.github.bsideup.jabel.Desugar;
import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@Desugar
public record Laser(double power) implements IRequiresEquals<Laser>, IIngredientType<Laser>, ITooltippable {

    @Override
    public boolean equalsTo(Laser other) {
        return this.power == other.power;
    }

    @Override
    public List<String> getTooltip() {
        return Lists.newArrayList(FormatUtils.format(I18n.format("jei.tooltip.modularmachineryaddons.laser"), String.valueOf(power)));
    }

    @Override
    public Class<? extends Laser> getIngredientClass() {
        return this.getClass();
    }
}
