package github.alecsio.mmceaddons.common.integration.jei.render;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Laser;
import github.alecsio.mmceaddons.common.integration.jei.render.base.BaseIngredientRenderer;
import net.minecraft.util.ResourceLocation;

public class LaserRenderer extends BaseIngredientRenderer<Laser> {
    @Override
    public ResourceLocation getTexture(Laser ingredient) {
        return new ResourceLocation(ModularMachineryAddons.MODID, "textures/gui/jei/jei_laser.png");
    }
}
