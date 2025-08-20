package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;
import net.minecraft.world.DimensionType;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class Dimension implements IRequiresEquals<Dimension>, IIngredientType<Dimension>, ITooltippable {

    private int id;
    private String name;

    public Dimension(int id) {
        this.id = id;
    }

    public Dimension(int id, String name) {
        this(id);
        this.name = name;
    }

    public Dimension() {}

    @Override
    public boolean equalsTo(Dimension other) {
        return this.id == other.id && Objects.equals(this.name, other.name);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    @Nonnull
    public Class<? extends Dimension> getIngredientClass() {
        return this.getClass();
    }

    // Client-side only
    @Override
    public List<String> getTooltip() {
        setDimensionNameIfNotPresent();
        return Lists.newArrayList(FormatUtils.format(I18n.format(LocalizationKeys.DIMENSION), name != null && !name.isEmpty() ? name : String.valueOf(id)));
    }

    public void setDimensionNameIfNotPresent() {
        if (name != null) {return;}

        if (DimensionManager.isDimensionRegistered(id)) {
            DimensionType type = DimensionManager.getProviderType(id);
            name = type.getName();
        }
    }
}
