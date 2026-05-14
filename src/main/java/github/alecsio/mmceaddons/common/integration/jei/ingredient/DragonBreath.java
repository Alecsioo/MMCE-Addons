package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.hatch.iceandfire.DragonType;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mcp.MethodsReturnNonnullByDefault;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;

import java.util.List;

@MethodsReturnNonnullByDefault
public class DragonBreath implements IRequiresEquals<DragonBreath>, IIngredientType<DragonBreath>, ITooltippable {

    private DragonType dragonType;
    private int amount;

    public DragonBreath(DragonType dragonType, int amount) {
        this.dragonType = dragonType;
        this.amount = amount;
    }

    public DragonBreath() {}

    public DragonType getDragonType() {
        return dragonType;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public boolean equalsTo(DragonBreath other) {
        return this.dragonType == other.dragonType && this.amount == other.amount;
    }

    @Override
    public List<String> getTooltip() {
        List<String> tooltip = Lists.newArrayList();

        tooltip.add(FormatUtils.format(I18n.format("jei.tooltip.modularmachineryaddons.breath_type"), I18n.format(String.format("dragon.type.%s", dragonType.name().toLowerCase()))));
        tooltip.add(FormatUtils.format(I18n.format("jei.tooltip.modularmachineryaddons.breath_amount"), String.valueOf(amount)));

        return tooltip;
    }

    @Override
    public Class<? extends DragonBreath> getIngredientClass() {
        return this.getClass();
    }
}
