package github.alecsio.mmceaddons.common.mixin;

import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHandler;
import nc.radiation.RadiationHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true, print = true)
@Pseudo
@Mixin(RadiationHandler.class)
public class RadiationHandlerMixin {

    @Inject(method = "mutateTerrain(Lnet/minecraft/world/World;Lnet/minecraft/world/chunk/Chunk;D)V",
            at = @At(value = "HEAD"),
            remap = false
    )
    private static void radiationHandlerMixin(World world, Chunk chunk, double radiation, CallbackInfo ci) {
        int dimensionId = world.provider.getDimension();
        if (ScrubbedChunksCache.isMarkedAsScrubbed(dimensionId, chunk.getPos())) {
            IRadiationSource chunkRadiationSource = RadiationHelper.getRadiationSource(chunk);
            chunkRadiationSource.setRadiationBuffer(0);
            chunkRadiationSource.setRadiationLevel(0);
        }
    }


}
