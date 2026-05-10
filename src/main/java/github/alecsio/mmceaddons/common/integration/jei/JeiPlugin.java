package github.alecsio.mmceaddons.common.integration.jei;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.integration.jei.helper.*;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.*;
import github.alecsio.mmceaddons.common.integration.jei.render.*;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IIngredientType;

import javax.annotation.Nonnull;

@JEIPlugin
public class JeiPlugin implements IModPlugin {

    private static final int DEFAULT_AMOUNT_UNUSED = 0;
    public static IGuiHelper GUI_HELPER;

    private IModIngredientRegistration registry;

    @Override
    public void register(@Nonnull IModRegistry registry) {
        GUI_HELPER = registry.getJeiHelpers().getGuiHelper();
    }

    @Override
    public void registerIngredients(@Nonnull IModIngredientRegistration registry) {
        this.registry = registry; // Storing it temporarily so I can avoid passing it 1000 times to registerIngredient :)

        registerIngredient(new Biome(), new BiomeHelper(), new BiomeRenderer());
        registerIngredient(new Dimension(), new DimensionHelper(), new DimensionRenderer());

        if (Mods.NUCLEARCRAFT.isPresent()) {
            registerIngredient(new Radiation(), new RadiationHelper(), new RadiationRenderer());
        }

        if (Mods.BLOODMAGIC.isPresent()) {
            registerIngredient(new Meteor(), new MeteorHelper(), new MeteorRenderer());
        }

        if (Mods.THAUMICENERGISTICS.isPresent()) {
            registerIngredient(new Essentia(), new EssentiaHelper(), new EssentiaRenderer());
        }

        if (Mods.THAUMCRAFT.isPresent()) {
            registerIngredient(new Flux(DEFAULT_AMOUNT_UNUSED, DEFAULT_AMOUNT_UNUSED), new FluxHelper(), new FluxRenderer());
            registerIngredient(new Vis(), new VisHelper(), new VisRenderer());
        }

        if (Mods.ABYSSALCRAFT.isPresent()) {
            registerIngredient(new PotentialEnergy(), new PotentialEnergyHelper(), new PotentialEnergyRenderer());
        }

        this.registry = null;
    }

    private <T> void registerIngredient(IIngredientType<T> ingredientType, IIngredientHelper<T> ingredientHelper, IIngredientRenderer<T> ingredientRenderer) {
        registry.register(ingredientType, Lists.newArrayList(), ingredientHelper, ingredientRenderer);
    }
}
