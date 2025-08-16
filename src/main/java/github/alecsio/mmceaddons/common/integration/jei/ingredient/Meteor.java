package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import WayofTime.bloodmagic.meteor.MeteorComponent;
import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.IntStream;

public class Meteor extends WayofTime.bloodmagic.meteor.Meteor implements IRequiresEquals<Meteor>, IIngredientType<Meteor>, ITooltippable {

    public Meteor() {
        super(null, Lists.newArrayList(), 0, 0);
    }

    public Meteor(ItemStack catalystStack, List<MeteorComponent> components, float explosionStrength, int radius) {
        super(catalystStack, components, explosionStrength, radius);
    }

    @Override
    public boolean equalsTo(Meteor other) {
        return this.getComponents().equals(other.getComponents()) &&
                this.getRadius() == other.getRadius() &&
                this.getExplosionStrength() == other.getExplosionStrength() &&
                IntStream.range(0, this.getComponents().size()).allMatch(i -> {
                    var otherComponent = other.getComponents().get(i);
                    var thisComponent = this.getComponents().get(i);
                    return thisComponent.getWeight() == otherComponent.getWeight() && thisComponent.getOreName().equals(otherComponent.getOreName());
                });
    }

    @Override
    @Nonnull
    public Class<? extends Meteor> getIngredientClass() {
        return this.getClass();
    }

    @Override
    public List<String> getTooltip() {
        List<String> tooltip = Lists.newArrayList();
        tooltip.add(FormatUtils.format(TextFormatting.BOLD, I18n.format(LocalizationKeys.METEOR_CONTENTS)));
        double total = this.getComponents().stream().mapToDouble(MeteorComponent::getWeight).sum();

        this.getComponents().forEach(component -> tooltip.add(FormatUtils.format(component.getOreName(), String.format("%.2f%%", (double)Math.round(component.weight/total*100)))));
        return tooltip;
    }
}
