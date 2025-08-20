package github.alecsio.mmceaddons.common.integration.jei.ingredient;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.integration.jei.IRequiresEquals;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.FormatUtils;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.formatting.ITooltippable;
import mezz.jei.api.recipe.IIngredientType;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public class Biome implements IRequiresEquals<Biome>, IIngredientType<Biome>, ITooltippable {

    private String registryName;
    private String name;

    public Biome() {}

    public Biome(String registryName) {
        this.registryName = registryName;
    }

    public Biome(String registryName, String name) {
        this(registryName);
        this.name = name;
    }

    @Override
    public boolean equalsTo(Biome other) {
        return this.registryName.equals(other.registryName) && Objects.equals(this.name, other.name);
    }

    public String getRegistryName() {
        return registryName;
    }

    public String getName() {
        return name;
    }

    @Override
    @Nonnull
    public Class<? extends Biome> getIngredientClass() {
        return this.getClass();
    }

    // Client-side only
    @Override
    public List<String> getTooltip() {
        setBiomeNameIfNotPresent();
        return Lists.newArrayList(FormatUtils.format(I18n.format(LocalizationKeys.BIOME), name != null && !name.isEmpty() ? name : registryName));
    }

    private void setBiomeNameIfNotPresent() {
        if (name != null) {return;}
        Object biomeObj = net.minecraft.world.biome.Biome.REGISTRY.getObject(new ResourceLocation(registryName));
        if (biomeObj instanceof net.minecraft.world.biome.Biome biome) {
            name = biome.getBiomeName();
        }
    }
}
