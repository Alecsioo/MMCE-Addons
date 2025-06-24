package github.alecsio.mmceaddons.common.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import github.alecsio.mmceaddons.common.cache.InterdimensionalChunkPos;
import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHandler;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(RadiationHandler.class)
public class RadiationHandlerMixin {

    @Inject(
            method = "updateWorldRadiation(Lnet/minecraftforge/fml/common/gameevent/TickEvent$WorldTickEvent;)V",
            at = @At(value = "INVOKE_ASSIGN", target = "Lnc/radiation/RadiationHelper;getRadiationSource(Lnet/minecraftforge/common/capabilities/ICapabilityProvider;)Lnc/capability/radiation/source/IRadiationSource;")
    , cancellable = true, remap = false
    )
    private void radiationSpreadMixin(TickEvent.WorldTickEvent event, CallbackInfo ci, @Local Chunk chunk, @Local IRadiationSource chunkRadiationSource) {
        World world = chunk.getWorld();
        if (ScrubbedChunksCache.isChunkScrubbed(InterdimensionalChunkPos.of(world.provider.getDimension(), chunk.getPos()), world)) {
            chunkRadiationSource.setRadiationBuffer(0);
            chunkRadiationSource.setRadiationLevel(0);
            ci.cancel();
        }
    }

    @ModifyArg(
            method = "updateWorldRadiation(Lnet/minecraftforge/fml/common/gameevent/TickEvent$WorldTickEvent;)V",
            at = @At(value = "INVOKE", target = "Lnc/radiation/RadiationHelper;spreadRadiationFromChunk(Lnet/minecraft/world/chunk/Chunk;Lnet/minecraft/world/chunk/Chunk;)V")
            , index = 1, remap = false
    )
    private Chunk radiationSpreadMixin(Chunk to) {
        if (to == null) return to;
        World world = to.getWorld();
        if (ScrubbedChunksCache.isChunkScrubbed(InterdimensionalChunkPos.of(world.provider.getDimension(), to.getPos()), world)) {
            return null;
        }
        return to;
    }


}
