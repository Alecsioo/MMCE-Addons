package github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache;

import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.TileScrubberProvider;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Utility class responsible for caching and managing the association between
 * interdimensional chunk positions and scrubber block positions.
 * <p>
 * This cache is used to quickly determine whether a given chunk is currently
 * being scrubbed by any {@link TileScrubberProvider}. The cache is updated
 * lazily, removing invalid or outdated scrubber references when queried.
 * </p>
 */
public class ScrubbedChunksCache {

    // Mapping of chunk positions (with dimension info) to number of active scrubbers.
    private static final ConcurrentMap<InterdimensionalChunkPos, AtomicInteger> SCRUBBED_CHUNKS_CACHE2 = new ConcurrentHashMap<>();

    /**
     * Adds one or more chunk positions to the scrubber cache, associating them with the given scrubber.
     * If the chunk is already associated with the scrubber, it will not be added again.
     *
     * @param chunkPos  The list of interdimensional chunk positions to cache.
     */
    public static void addChunksToCache(List<InterdimensionalChunkPos> chunkPos) {
        for (InterdimensionalChunkPos interdimensionalChunkPos : chunkPos) {
            SCRUBBED_CHUNKS_CACHE2.computeIfAbsent(interdimensionalChunkPos, k -> new AtomicInteger(0));
            SCRUBBED_CHUNKS_CACHE2.get(interdimensionalChunkPos).incrementAndGet();
        }
    }

    /**
     * Checks whether the specified chunk is currently being scrubbed.
     * Invalid or stale scrubber references (e.g., missing tile entities or inactive scrubbers)
     * are removed during this check.
     *
     * @param chunkPos  The interdimensional chunk position to check.
     * @return {@code true} if any valid scrubber is actively scrubbing the chunk; {@code false} otherwise.
     */
    public static boolean isChunkScrubbed(InterdimensionalChunkPos chunkPos) {
        AtomicInteger scrubbers = SCRUBBED_CHUNKS_CACHE2.get(chunkPos);
        return scrubbers != null && scrubbers.get() > 0;
    }

    /**
     * Returns a formatted string containing all cached chunk-to-scrubber mappings.
     * Primarily intended for debugging or logging purposes.
     *
     * @return A string representation of the current scrubber cache state.
     */
    public static String getInformation() {
        StringBuilder stringBuilder = new StringBuilder();

        SCRUBBED_CHUNKS_CACHE2.forEach((chunkPos, count) ->
                stringBuilder.append(chunkPos)
                        .append(" -> [total: ").append(count.get()).append("]\n")
        );

        return stringBuilder.toString();
    }

    public static void removeScrubbedChunks(List<InterdimensionalChunkPos> scrubbedChunks) {
        for (InterdimensionalChunkPos scrubbedChunk : scrubbedChunks) {
            AtomicInteger counter = SCRUBBED_CHUNKS_CACHE2.get(scrubbedChunk);
            if (counter != null) {
                counter.decrementAndGet();
            }
        }
    }
}
