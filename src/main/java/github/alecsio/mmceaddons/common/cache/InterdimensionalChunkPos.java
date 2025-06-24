package github.alecsio.mmceaddons.common.cache;

import com.github.bsideup.jabel.Desugar;
import net.minecraft.util.math.ChunkPos;

/**
 * Represents a chunk position with dimension information. Although somewhat redundant (because Chunk already holds all that information)
 * this class was created to avoid passing around references to such core objects, since I only need a very small subset
 * of its information.
 *
 * @param dimensionId       the id of the dimension where this chunk pos is
 * @param chunkPosAsLong    the chunk position, represented as long
 */
@Desugar
public record InterdimensionalChunkPos(int dimensionId, long chunkPosAsLong) {
    public static InterdimensionalChunkPos of(int dimId, ChunkPos chunkPos) {
        return new InterdimensionalChunkPos(dimId, ChunkPos.asLong(chunkPos.x, chunkPos.z));
    }

    public static InterdimensionalChunkPos of(int dimId, Long chunkPosAsLong) {
        return new InterdimensionalChunkPos(dimId, chunkPosAsLong);
    }

    // Meh, but it's only needed for logging
    @Override
    public String toString() {
        return String.format("InterDimensionalChunkPos{ChunkPos=%d, dimensionId=%d}", chunkPosAsLong, dimensionId);
    }
}
