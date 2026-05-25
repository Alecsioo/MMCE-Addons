package github.alecsio.mmceaddons.common.hatch.thaumcraft.flux;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshot;
import github.alecsio.mmceaddons.common.hatch.ISnapshot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;

public class FluxSnapshot extends AbstractSnapshot<RequirementFlux> {

    private final Map<Long, Float> chunkToFlux;

    public FluxSnapshot(int coveredRange, BlockPos center, Map<Long, Float> chunkToFlux) {
        super(coveredRange, center);
        this.chunkToFlux = chunkToFlux;
    }

    @Override
    public ISnapshot<RequirementFlux> getSnapshotForRange(int range) {
        ChunkPos center = new ChunkPos(this.center);
        Map<Long, Float> result = new HashMap<>();

        for (Map.Entry<Long, Float> entry : chunkToFlux.entrySet()) {
            if (isChunkWithinRange(entry.getKey(), center, range)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return new FluxSnapshot(coveredRange, this.center, result);
    }

    @Override
    public boolean canHandleInput(RequirementFlux requirement) {
        if (requirement.getChunkRange() > coveredRange) return false;

        double needed = requirement.getAmount();
        double accumulated = 0.0D;
        double minPerChunk = requirement.getMinPerChunk();

        for (float stored : chunkToFlux.values()) {
            accumulated += Math.max(0.0D, stored - minPerChunk);
            if (accumulated >= needed) return true;
        }

        return false;
    }

    @Override
    public boolean canHandleOutput(RequirementFlux requirement) {
        if (requirement.getChunkRange() > coveredRange) return false;

        double needed = requirement.getAmount();
        double accumulated = 0.0D;
        double maxPerChunk = requirement.getMaxPerChunk();

        for (float stored : chunkToFlux.values()) {
            accumulated += Math.max(0.0D, maxPerChunk - stored);
            if (accumulated >= needed) return true;
        }

        return false;
    }
}
