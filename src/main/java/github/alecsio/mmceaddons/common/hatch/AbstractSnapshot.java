package github.alecsio.mmceaddons.common.hatch;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public abstract class AbstractSnapshot<T extends IMultiChunkRequirement> implements ISnapshot<T> {

    protected final int coveredRange;
    protected final BlockPos center;

    public AbstractSnapshot(int coveredRange, BlockPos center) {
        this.coveredRange = coveredRange;
        this.center = center;
    }

    public int getCoveredRange() {
        return coveredRange;
    }

    protected boolean isChunkWithinRange(long chunkPosLong, ChunkPos center, int range) {
        int chunkX = getChunkX(chunkPosLong);
        int chunkZ = getChunkZ(chunkPosLong);

        return Math.abs(chunkX - center.x) <= range
                && Math.abs(chunkZ - center.z) <= range;
    }

    protected int getChunkX(long chunkPosLong) {
        return (int) chunkPosLong;
    }

    protected int getChunkZ(long chunkPosLong) {
        return (int) (chunkPosLong >>> 32);
    }
}
