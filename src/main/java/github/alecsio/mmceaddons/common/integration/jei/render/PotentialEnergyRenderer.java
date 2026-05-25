package github.alecsio.mmceaddons.common.integration.jei.render;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import github.alecsio.mmceaddons.common.integration.jei.render.base.BaseIngredientRenderer;
import net.minecraft.util.ResourceLocation;

public class PotentialEnergyRenderer extends BaseIngredientRenderer<PotentialEnergy> {

    @Override
    public ResourceLocation getTexture(PotentialEnergy ingredient) {
        return new ResourceLocation(Mods.ABYSSALCRAFT_ID, "textures/items/necronomicon.png");
    }
}
