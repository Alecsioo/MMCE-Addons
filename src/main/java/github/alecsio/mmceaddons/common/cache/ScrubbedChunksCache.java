package github.alecsio.mmceaddons.common.cache;

import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Holds a per-dimension map of scrubbed chunk positions and the count of scrubbers currently affecting the given chunkpos.
 * This is to prevent hatches affecting the same chunk from prematurely unregistering a chunk from the scrubbed chunks.
 * This data is used in the nc.radiation.RadiationHandler#updateWorldRadiation(net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent)
 * to detect which chunks are actively being scrubbed, and apply the MMCE:A scrubbing behaviour to them directly in the
 * NC handler
 */
public class ScrubbedChunksCache {

    private static final Logger LOGGER = LogManager.getLogger();

    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private static final ConcurrentMap<Integer, ConcurrentMap<ChunkPos, Integer>> DIMENSION_SCRUBBED_CHUNKS_MARKERS = new ConcurrentHashMap<>();


    public static void markAsScrubbed(int dimensionId, ChunkPos chunkPos) {
        ConcurrentMap<ChunkPos, Integer> chunkMap = DIMENSION_SCRUBBED_CHUNKS_MARKERS.computeIfAbsent(dimensionId, k -> new ConcurrentHashMap<>());
        chunkMap.merge(chunkPos, 1, Integer::sum);
    }

    public static void unmarkAsScrubbed(int dimensionId, ChunkPos chunkPos) {
        ConcurrentMap<ChunkPos, Integer> chunkMap = DIMENSION_SCRUBBED_CHUNKS_MARKERS.get(dimensionId);
        if (chunkMap == null) {
            LOGGER.error("No marked chunks found for dimension {}. Cache is in inconsistent state!", dimensionId);
            return;
        }

        chunkMap.compute(chunkPos, (k, scrubberCount) -> {
            if (scrubberCount == null || scrubberCount <= 1) {return null;} // Remove if the chunk is not scrubbed by any scrubber hatches anymore
            return scrubberCount - 1;
        });
    }

    public static boolean isMarkedAsScrubbed(int dimensionId, ChunkPos chunkPos) {
        try {
            lock.readLock().lock();

            ConcurrentMap<ChunkPos, Integer> chunkMap = DIMENSION_SCRUBBED_CHUNKS_MARKERS.get(dimensionId);
            return chunkMap != null && chunkMap.containsKey(chunkPos);

        } finally {
            lock.readLock().unlock();
        }
    }

    public static void unmarkAllAsScrubbed(int dimensionId, List<ChunkPos> chunkPositions) {
        try {
            lock.writeLock().lock();
            chunkPositions.forEach(chunkPos -> unmarkAsScrubbed(dimensionId, chunkPos));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void unmarkAllAsScrubbed(int dimensionId) {
        DIMENSION_SCRUBBED_CHUNKS_MARKERS.remove(dimensionId);
    }
}
