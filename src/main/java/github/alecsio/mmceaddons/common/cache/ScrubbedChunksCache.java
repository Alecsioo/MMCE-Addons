package github.alecsio.mmceaddons.common.cache;

import github.alecsio.mmceaddons.common.tile.nuclearcraft.TileScrubberProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

    private static final Logger LOGGER = LogManager.getLogger();

    // Mapping of chunk positions (with dimension info) to scrubber block positions.
    private static final ConcurrentMap<InterdimensionalChunkPos, CopyOnWriteArrayList<BlockPos>> SCRUBBED_CHUNKS_CACHE = new ConcurrentHashMap<>();

    /**
     * Adds one or more chunk positions to the scrubber cache, associating them with the given scrubber.
     * If the chunk is already associated with the scrubber, it will not be added again.
     *
     * @param chunkPos  The list of interdimensional chunk positions to cache.
     * @param scrubber  The position of the scrubber block responsible for scrubbing these chunks.
     */
    public static void addChunksToCache(List<InterdimensionalChunkPos> chunkPos, BlockPos scrubber) {
        List<BlockPos> blockPos = new ArrayList<>();
        blockPos.add(scrubber);

        for (InterdimensionalChunkPos interdimensionalChunkPos : chunkPos) {
            SCRUBBED_CHUNKS_CACHE.computeIfAbsent(interdimensionalChunkPos, ignored -> new CopyOnWriteArrayList<>(blockPos));
            SCRUBBED_CHUNKS_CACHE.get(interdimensionalChunkPos).addIfAbsent(scrubber);
        }
    }

    /**
     * Checks whether the specified chunk is currently being scrubbed.
     * Invalid or stale scrubber references (e.g., missing tile entities or inactive scrubbers)
     * are removed during this check.
     *
     * @param chunkPos  The interdimensional chunk position to check.
     * @param world     The world to look up tile entities in.
     * @return {@code true} if any valid scrubber is actively scrubbing the chunk; {@code false} otherwise.
     */
    public static boolean isChunkScrubbed(InterdimensionalChunkPos chunkPos, World world) {
        CopyOnWriteArrayList<BlockPos> positions = SCRUBBED_CHUNKS_CACHE.get(chunkPos);
        if (positions == null || positions.isEmpty()) return false;
        Iterator<BlockPos> iterator = positions.iterator();

        List<BlockPos> invalidPositions = new ArrayList<>();

        try {
            while (iterator.hasNext()) {
                BlockPos pos = iterator.next();
                TileEntity tileEntity = world.getTileEntity(pos);
                if (!(tileEntity instanceof TileScrubberProvider provider) || !provider.canScrubChunk(chunkPos.chunkPosAsLong())) {
                    invalidPositions.add(pos);
                    continue;
                }

                return true;
            }
        } finally { // Regardless of early return or not, process invalid positions
            positions.removeAll(invalidPositions);
            invalidPositions.forEach(pos -> LOGGER.info("Removed invalid scrubber position: {}. For chunk: {}", pos, chunkPos));

            if (positions.isEmpty()) {
                SCRUBBED_CHUNKS_CACHE.remove(chunkPos);
                LOGGER.info("Removed empty chunk entry {} from cache", chunkPos);
            }
        }

        return false;
    }

    /**
     * Returns a formatted string containing all cached chunk-to-scrubber mappings.
     * Primarily intended for debugging or logging purposes.
     *
     * @return A string representation of the current scrubber cache state.
     */
    public static String getInformation() {
        StringBuilder stringBuilder = new StringBuilder();

        SCRUBBED_CHUNKS_CACHE.forEach((chunkPos, positions) -> {
            int count = positions.size();
            stringBuilder.append(chunkPos)
                    .append(" -> [total: ").append(count).append(", positions: ");

            positions.forEach(pos -> stringBuilder.append(pos).append(", "));

            // Remove trailing ", " if present
            int len = stringBuilder.length();
            if (count > 0) {
                stringBuilder.setLength(len - 2); // strip last ", "
            }

            stringBuilder.append("]\n");
        });

        return stringBuilder.toString();
    }
}
