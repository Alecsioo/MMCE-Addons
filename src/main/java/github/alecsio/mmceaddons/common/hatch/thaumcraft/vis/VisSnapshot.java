package github.alecsio.mmceaddons.common.hatch.thaumcraft.vis;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshot;
import github.alecsio.mmceaddons.common.hatch.ISnapshot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;

public class VisSnapshot extends AbstractSnapshot<RequirementVis> {

    private final Map<Long, Float> chunkToVis;

    public VisSnapshot(int coveredRange, BlockPos center, Map<Long, Float> chunkToVis) {
        super(coveredRange, center);
        this.chunkToVis = chunkToVis;
    }

    @Override
    public ISnapshot<RequirementVis> getSnapshotForRange(int range) {
        ChunkPos center = new ChunkPos(this.center);
        Map<Long, Float> result = new HashMap<>();

        for (Map.Entry<Long, Float> entry : chunkToVis.entrySet()) {
            if (isChunkWithinRange(entry.getKey(), center, range)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return new VisSnapshot(coveredRange, this.center, result);
    }

    @Override
    public boolean canHandleInput(RequirementVis requirement) {
        if (requirement.getChunkRange() > coveredRange) return false;

        double needed = requirement.getAmount();
        double accumulated = 0.0D;
        double minPerChunk = requirement.getMinPerChunk();

        for (float stored : chunkToVis.values()) {
            accumulated += Math.max(0.0D, stored - minPerChunk);
            if (accumulated >= needed) return true;
        }

        return false;
    }

    @Override
    public boolean canHandleOutput(RequirementVis requirement) {
        if (requirement.getChunkRange() > coveredRange) return false;

        double needed = requirement.getAmount();
        double accumulated = 0.0D;
        double maxPerChunk = requirement.getMaxPerChunk();

        for (float stored : chunkToVis.values()) {
            accumulated += Math.max(0.0D, maxPerChunk - stored);
            if (accumulated >= needed) return true;
        }

        return false;
    }
}
