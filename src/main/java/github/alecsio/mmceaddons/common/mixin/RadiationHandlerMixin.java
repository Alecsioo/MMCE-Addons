package github.alecsio.mmceaddons.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache.InterdimensionalChunkPos;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache.ScrubbedChunksCache;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.WorldRadiationHandler;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(WorldRadiationHandler.class)
public class RadiationHandlerMixin {

    @Inject(
            method = "updateInternal(Lnet/minecraft/world/WorldServer;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnc/radiation/RadiationHelper;getRadiationSource(Lnet/minecraftforge/common/capabilities/ICapabilityProvider;)Lnc/capability/radiation/source/IRadiationSource;")
            , cancellable = true, remap = false
    )
    private void radiationSpreadMixin(WorldServer world, CallbackInfo ci, @Local(name = "chunk") Chunk chunk, @Local(name = "chunkSource") IRadiationSource chunkRadiationSource) {
        if (ScrubbedChunksCache.isChunkScrubbed(InterdimensionalChunkPos.of(world.provider.getDimension(), chunk.getPos()))) {
            chunkRadiationSource.setRadiationBuffer(0);
            chunkRadiationSource.setRadiationLevel(0);
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "updateInternal(Lnet/minecraft/world/WorldServer;)V",
            at = @At(value = "INVOKE", target = "Lnc/radiation/RadiationHelper;spreadRadiationFromChunk(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/chunk/Chunk;)V")
            , index = 1, remap = false
    )
    private Chunk radiationSpreadMixin(Chunk to) {
        if (to == null) return to;
        World world = to.getWorld();
        if (ScrubbedChunksCache.isChunkScrubbed(InterdimensionalChunkPos.of(world.provider.getDimension(), to.getPos()))) {
            return null;
        }
        return to;
    }

}
