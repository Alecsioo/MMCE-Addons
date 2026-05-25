package github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import github.alecsio.mmceaddons.common.hatch.ISnapshot;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.handler.chunks.ChunksReader;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;
import thaumcraft.api.aura.AuraHelper;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TileRadiationProvider extends AbstractSnapshotMachineComponent<RequirementRadiation> implements MachineComponentTile {

    protected RadiationSnapshot radiationSnapshot;
    protected final ChunksReader chunksReader = ChunksReader.getInstance();
    protected volatile int largestChunkRange = 0;

    public BlockPos getBlockInChunk(Chunk chunk) {
        // Get chunk's starting block position
        int chunkStartX = chunk.getPos().x * 16;
        int chunkStartZ = chunk.getPos().z * 16;

        return new BlockPos(chunkStartX, 0, chunkStartZ);
    }

    protected String getKeyForRequirement(RequirementRadiation requirement) {
        return requirement.getIOType().equals(IOType.INPUT) ? "error.modularmachineryaddons.requirement.missing.multichunk.input" : "error.modularmachineryaddons.requirement.missing.multichunk.output";
    }

    @Override
    protected void updateSnapshot() {
        Map<Long, Double> localChunkToRads = new HashMap<>();

        for (Chunk chunk : chunksReader.getSurroundingChunks(this.world, this.pos, largestChunkRange)) {
            long chunkAsLong = ChunkPos.asLong(chunk.x, chunk.z);
            IRadiationSource radiationSource = RadiationHelper.getRadiationSource(chunk);

            if (radiationSource == null) {
                continue;
            }

            double radiationAmount = radiationSource.getRadiationLevel();
            localChunkToRads.put(chunkAsLong, radiationAmount);
        }

        RadiationSnapshot localSnapshot = new RadiationSnapshot(largestChunkRange, this.pos, localChunkToRads);

        lock.writeLock().lock();
        try {
            this.radiationSnapshot = localSnapshot;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static class Input extends TileRadiationProvider {
        @Nullable
        @Override
        public MachineComponent<IRequirementHandler<RequirementRadiation>> provideComponent() {
            return new MachineComponentRadiationProvider(IOType.INPUT, this);
        }

        @Override
        protected CraftCheck checkSnapshot(RequirementRadiation requirement) {
            if (requirement.getChunkRange() > radiationSnapshot.getCoveredRange()) {
                largestChunkRange = requirement.getChunkRange();
                return CraftCheck.failure(getKeyForRequirement(requirement));
            }

            ISnapshot<RequirementRadiation> snapshot = radiationSnapshot.getSnapshotForRange(requirement.getChunkRange());
            return snapshot.canHandleInput(requirement) ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(requirement));
        }

        @Override
        public void handle(RequirementRadiation requirement) {
            List<Chunk> chunks = chunksReader.getSurroundingChunks(this.world, this.pos, requirement.getChunkRange());
            double remaining = requirement.getAmount();

            double minPerChunk = requirement.getMinPerChunk();

            for (Chunk chunk : chunks) {
                if (remaining <= 0.0D) break;

                BlockPos pos = getBlockInChunk(chunk);
                float stored = AuraHelper.getFlux(this.world, pos);
                double available = Math.max(0.0D, stored - minPerChunk);
                if (available <= 0.0D) continue;

                double toDrain = Math.min(available, remaining);
                AuraHelper.drainFlux(this.world, pos, (float) toDrain, false);
                remaining -= toDrain;
            }

            super.handle(requirement);
        }
    }

    public static class Output extends TileRadiationProvider {
        @Nullable
        @Override
        public MachineComponent<IRequirementHandler<RequirementRadiation>> provideComponent() {
            return new MachineComponentRadiationProvider(IOType.OUTPUT, this);
        }

        @Override
        protected CraftCheck checkSnapshot(RequirementRadiation requirement) {
            if (requirement.getChunkRange() > radiationSnapshot.getCoveredRange()) {
                largestChunkRange = requirement.getChunkRange();
                return CraftCheck.failure(getKeyForRequirement(requirement));
            }

            ISnapshot<RequirementRadiation> snapshot = radiationSnapshot.getSnapshotForRange(requirement.getChunkRange());
            return snapshot.canHandleOutput(requirement) ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(requirement));
        }

        @Override
        public void handle(RequirementRadiation requirement) {
            List<Chunk> chunks = chunksReader.getSurroundingChunks(this.world, this.pos, requirement.getChunkRange());
            double remaining = requirement.getAmount();

            chunks.sort(Comparator.comparingDouble(chunk -> AuraHelper.getFlux(this.world, getBlockInChunk(chunk))));

            double maxPerChunk = requirement.getMaxPerChunk();

            for (Chunk chunk : chunks) {
                if (remaining <= 0.0D) break;

                BlockPos pos = getBlockInChunk(chunk);
                float stored = AuraHelper.getFlux(this.world, pos);
                double capacity = Math.max(0.0D, maxPerChunk - stored);
                if (capacity <= 0.0D) continue;

                double toFill = Math.min(capacity, remaining);
                AuraHelper.polluteAura(this.world, pos, (float) toFill, true);
                remaining -= toFill;
            }

            super.handle(requirement);
        }
    }
}
