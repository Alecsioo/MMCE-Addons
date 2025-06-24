package github.alecsio.mmceaddons.common.cache.handler;

import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldUnloadHandler {

    @SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload event) {
        ScrubbedChunksCache.unmarkAllAsScrubbed(event.getWorld().provider.getDimension());
    }
}
