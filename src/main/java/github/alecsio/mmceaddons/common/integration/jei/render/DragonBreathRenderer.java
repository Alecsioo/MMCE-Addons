package github.alecsio.mmceaddons.common.integration.jei.render;

import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.iceandfire.DragonType;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import github.alecsio.mmceaddons.common.integration.jei.render.base.BaseIngredientRenderer;
import kport.modularmagic.common.integration.JeiPlugin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DragonBreathRenderer extends BaseIngredientRenderer<DragonBreath> {

    private static final Map<DragonType, Color> COLORS = new HashMap<>();

    static {
        COLORS.put(DragonType.FIRE, new Color(229, 79, 6));
        COLORS.put(DragonType.ICE, new Color(99, 253, 255));
        COLORS.put(DragonType.LIGHTNING, new Color(140, 43, 253));
    }


    @Override
    public void render(@Nonnull Minecraft minecraft, int x, int y, @Nullable DragonBreath toRender) {
        if (toRender == null) {
            return;
        }

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();

        Color c = COLORS.get(toRender.getDragonType());
        GlStateManager.color((float) c.getRed() / 255.0F, (float) c.getGreen() / 255.0F, (float) c.getBlue() / 255.0F, 1.0F);

        ResourceLocation texture = getTexture(toRender);
        builder = JeiPlugin.GUI_HELPER.drawableBuilder(texture, 0, 0, 16, 16);
        builder.setTextureSize(16, 16);
        builder.build().draw(minecraft, x, y);

        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    @Override
    public ResourceLocation getTexture(DragonBreath ingredient) {
        return new ResourceLocation(Mods.ICE_AND_FIRE_ID, "textures/items/dragon_skull_fire.png");
    }
}
