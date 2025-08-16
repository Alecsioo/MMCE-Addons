package github.alecsio.mmceaddons.client.render.entity;

import github.alecsio.mmceaddons.common.entity.EntityImprovedMeteor;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;

public class MeteorRenderFactory implements IRenderFactory<EntityImprovedMeteor> {
    @Override
    public Render<? super EntityImprovedMeteor> createRenderFor(RenderManager manager) {
        return new RenderEntityMeteorWrapper(manager);
    }
}
