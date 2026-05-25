package github.alecsio.mmceaddons.common.hatch.bloodmagic.will;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import github.alecsio.mmceaddons.common.hatch.AbstractSnapshot;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

import java.util.HashMap;
import java.util.Map;

public class DemonWillSnapshot extends AbstractSnapshot<RequirementWillMultiChunk> {

    private final Map<Long, Map<EnumDemonWillType, Double>> chunkToWill;

    public DemonWillSnapshot(Map<Long, Map<EnumDemonWillType, Double>> chunkToWill, int coveredRange, BlockPos pos) {
        super(coveredRange, pos);
        this.chunkToWill = chunkToWill;
    }

    @Override
    public DemonWillSnapshot getSnapshotForRange(int range) {
        ChunkPos center = new ChunkPos(this.center);
        Map<Long, Map<EnumDemonWillType, Double>> result = new HashMap<>();

        for (Map.Entry<Long, Map<EnumDemonWillType, Double>> entry : chunkToWill.entrySet()) {
            if (isChunkWithinRange(entry.getKey(), center, range)) {
                result.put(entry.getKey(), entry.getValue());
            }
        }

        return new DemonWillSnapshot(result, coveredRange, this.center);
    }

    @Override
    public boolean canHandleInput(RequirementWillMultiChunk requirement) {
        int chunkRange = requirement.getChunkRange();
        if (chunkRange > coveredRange) {
            return false;
        }

        EnumDemonWillType type = requirement.willType;
        double amountToDrain = requirement.getAmount();
        double amountDrained = 0.0D;
        double minPerChunk = requirement.getMinPerChunk();

        for (Map<EnumDemonWillType, Double> typeToAmount : chunkToWill.values()) {
            double storedAmount = typeToAmount.getOrDefault(type, 0.0D);

            double availableToDrain = Math.max(0.0D, storedAmount - minPerChunk);
            amountDrained += availableToDrain;

            if (amountDrained >= amountToDrain) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canHandleOutput(RequirementWillMultiChunk requirement) {
        int chunkRange = requirement.getChunkRange();
        if (chunkRange > coveredRange) {
            return false;
        }

        EnumDemonWillType type = requirement.willType;
        double amountToInsert = requirement.getAmount();
        double amountInsertable = 0.0D;
        double maxPerChunk = requirement.getMaxPerChunk();

        for (Map<EnumDemonWillType, Double> typeToAmount : chunkToWill.values()) {
            double storedAmount = typeToAmount.getOrDefault(type, 0.0D);

            double availableCapacity = Math.max(0.0D, maxPerChunk - storedAmount);
            amountInsertable += availableCapacity;

            if (amountInsertable >= amountToInsert) {
                return true;
            }
        }

        return false;
    }
}
