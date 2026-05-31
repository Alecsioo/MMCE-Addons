package github.alecsio.mmceaddons.common.hatch.nuclearcraft;

import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache.InterdimensionalChunkPos;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache.ScrubbedChunksCache;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

public class RadiationHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void updatePlayerRadiation(TickEvent.PlayerTickEvent event) {
        if (!NCConfig.radiation_enabled_public || event.side == Side.CLIENT || event.phase == TickEvent.Phase.END) {
            return;
        }

        EntityPlayer player = event.player;
        World world = player.world;
        Chunk chunk = world.getChunk(player.chunkCoordX, player.chunkCoordZ);
        if (ScrubbedChunksCache.isChunkScrubbed(InterdimensionalChunkPos.of(world.provider.getDimension(), chunk.getPos()))) {
            IRadiationSource source = RadiationHelper.getRadiationSource(chunk);
            if (source != null) {
                source.setRadiationLevel(0);
                source.setRadiationBuffer(0);
            }
            IEntityRads entityRads = RadiationHelper.getEntityRadiation(player);
            if (entityRads != null) {
                entityRads.setRadiationLevel(0);
            }
        }
    }
}
