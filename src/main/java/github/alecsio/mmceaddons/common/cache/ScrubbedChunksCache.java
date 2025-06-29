package github.alecsio.mmceaddons.common.cache;

import net.minecraft.util.math.ChunkPos;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private static final HashMap<Integer, HashMap<ChunkPos, Integer>> DIMENSION_SCRUBBED_CHUNKS_MARKERS = new HashMap<>();
    private static final Map<InterDimensionalBlockPos, Boolean> SCRUBBER_POS_IS_SCRUBBED = new HashMap<>();

    public static void markAsScrubbed(InterDimensionalBlockPos controllerPosition, List<ChunkPos> scrubbedChunks) {
        try {
            lock.writeLock().lock();

            SCRUBBER_POS_IS_SCRUBBED.put(controllerPosition, true);
            HashMap<ChunkPos, Integer> affectedChunks = DIMENSION_SCRUBBED_CHUNKS_MARKERS.computeIfAbsent(controllerPosition.dimensionId(), dimId -> new HashMap<>());
            scrubbedChunks.forEach(chunkPos -> affectedChunks.merge(chunkPos, 1, Integer::sum));

        } finally {
            lock.writeLock().unlock();
        }
    }

    public static void unmarkAsScrubbed(InterDimensionalBlockPos controllerPosition, List<ChunkPos> scrubbedChunks) {
        try {
            lock.writeLock().lock();

            SCRUBBER_POS_IS_SCRUBBED.remove(controllerPosition);
            HashMap<ChunkPos, Integer> affectedChunks = DIMENSION_SCRUBBED_CHUNKS_MARKERS.computeIfAbsent(controllerPosition.dimensionId(), dimId -> new HashMap<>());

            for (final ChunkPos chunkPos : scrubbedChunks) {
                affectedChunks.compute(chunkPos, (scrubbedChunk, scrubbedCount) -> {
                    if (scrubbedCount == null || scrubbedCount <= 1) {
                        return null;
                    }
                    return scrubbedCount - 1;
                });
            }

        } finally {
            lock.writeLock().unlock();
        }
    }

    public static boolean isScrubbed(InterDimensionalBlockPos controllerPosition) {
        Boolean isScrubbed;
        try {
            lock.readLock().lock();
            isScrubbed = SCRUBBER_POS_IS_SCRUBBED.get(controllerPosition);
        } finally {
            lock.readLock().unlock();
        }
        return isScrubbed != null && isScrubbed;
    }

    public static boolean isChunkMarkedAsScrubbed(int dimensionId, ChunkPos chunkPos) {
        try {
            lock.readLock().lock();

            HashMap<ChunkPos, Integer> chunkMap = DIMENSION_SCRUBBED_CHUNKS_MARKERS.get(dimensionId);
            return chunkMap != null && chunkMap.containsKey(chunkPos);

        } finally {
            lock.readLock().unlock();
        }
    }

    public static void unmarkAllAsScrubbed(int dimensionId) {
        DIMENSION_SCRUBBED_CHUNKS_MARKERS.remove(dimensionId);
    }
}
