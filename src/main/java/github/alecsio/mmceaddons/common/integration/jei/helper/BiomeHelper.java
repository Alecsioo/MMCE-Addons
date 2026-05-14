package github.alecsio.mmceaddons.common.integration.jei.helper;

import github.alecsio.mmceaddons.common.integration.jei.helper.base.BaseIngredientHelper;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Biome;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BiomeHelper extends BaseIngredientHelper<Biome> {

    @Override
    public String getUniqueId(Biome biome) {
        return biome.getRegistryName();
    }

    @Override
    public String getResourceId(Biome biome) {
        return getUniqueId(biome);
    }

    @Override
    public Biome copyIngredient(Biome biome) {
        return new Biome(biome.getRegistryName(), biome.getName());
    }
}
