package github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshot;
import github.alecsio.mmceaddons.common.hatch.ISnapshot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;

public class RadiationSnapshot extends AbstractSnapshot<RequirementRadiation> {

    private final Map<Long, Double> chunkToRads;

    public RadiationSnapshot(int coveredRange, BlockPos center, Map<Long, Double> chunkToRads) {
        super(coveredRange, center);
        this.chunkToRads = chunkToRads;
    }

    @Override
    public ISnapshot<RequirementRadiation> getSnapshotForRange(int range) {
        ChunkPos center = new ChunkPos(this.center);
        Map<Long, Double> result = new HashMap<>();

        for (Map.Entry<Long, Double> entry : chunkToRads.entrySet()) {
            if (isChunkWithinRange(entry.getKey(), center, range)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return new RadiationSnapshot(coveredRange, this.center, result);
    }

    @Override
    public boolean canHandleInput(RequirementRadiation requirement) {
        if (requirement.getChunkRange() > coveredRange) return false;

        double needed = requirement.getAmount();
        double accumulated = 0.0D;
        double minPerChunk = requirement.getMinPerChunk();

        for (double stored : chunkToRads.values()) {
            accumulated += Math.max(0.0D, stored - minPerChunk);
            if (accumulated >= needed) return true;
        }

        return false;
    }

    @Override
    public boolean canHandleOutput(RequirementRadiation requirement) {
        if (requirement.getChunkRange() > coveredRange) return false;

        double needed = requirement.getAmount();
        double accumulated = 0.0D;
        double maxPerChunk = requirement.getMaxPerChunk();

        for (double stored : chunkToRads.values()) {
            accumulated += Math.max(0.0D, maxPerChunk - stored);
            if (accumulated >= needed) return true;
        }

        return false;
    }
}
