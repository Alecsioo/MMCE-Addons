package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import WayofTime.bloodmagic.demonAura.WorldDemonWillHandler;
import WayofTime.bloodmagic.soul.EnumDemonWillType;
import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import github.alecsio.mmceaddons.common.hatch.handler.chunks.ChunksReader;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TileWillMultiChunkProvider extends AbstractSnapshotMachineComponent<RequirementWillMultiChunk> implements MachineComponentTile {

    protected DemonWillSnapshot demonWillSnapshot;
    protected final ChunksReader chunksReader = ChunksReader.getInstance();
    protected volatile int largestChunkRange = 0;

    public BlockPos getBlockInChunk(Chunk chunk) {
        // Get chunk's starting block position
        int chunkStartX = chunk.getPos().x * 16;
        int chunkStartZ = chunk.getPos().z * 16;

        return new BlockPos(chunkStartX, 0, chunkStartZ);
    }

    protected String getKeyForRequirement(RequirementWillMultiChunk requirement) {
        return requirement.getIOType().equals(IOType.INPUT) ? "error.modularmachineryaddons.requirement.missing.multichunk.input" : "error.modularmachineryaddons.requirement.missing.multichunk.output";
    }

    @Override
    protected void updateSnapshot() {
        Map<Long, Map<EnumDemonWillType, Double>> localChunkToWill = new HashMap<>();

        for (Chunk chunk : chunksReader.getSurroundingChunks(this.world, this.pos, largestChunkRange)) {
            long chunkAsLong = ChunkPos.asLong(chunk.x, chunk.z);
            Map<EnumDemonWillType, Double> willByType = new HashMap<>();

            for (EnumDemonWillType type : EnumDemonWillType.values()) {
                double willAmount = WorldDemonWillHandler.getCurrentWill(this.world, getBlockInChunk(chunk), type);
                willByType.put(type, willAmount);
            }

            localChunkToWill.put(chunkAsLong, willByType);
        }

        DemonWillSnapshot localSnapshot = new DemonWillSnapshot(localChunkToWill, largestChunkRange, this.pos);

        lock.writeLock().lock();
        try {
            this.demonWillSnapshot = localSnapshot;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public static class Input extends TileWillMultiChunkProvider {
        @Nullable
        @Override
        public MachineComponentWillMultiChunkProvider provideComponent() {
            return new MachineComponentWillMultiChunkProvider(IOType.INPUT, this) {
            };
        }

        @Override
        protected CraftCheck checkSnapshot(RequirementWillMultiChunk requirement) {
            if (requirement.getChunkRange() > demonWillSnapshot.getCoveredRange()) {
                largestChunkRange = requirement.getChunkRange();
                return CraftCheck.failure(getKeyForRequirement(requirement));
            }

            DemonWillSnapshot snapshot = demonWillSnapshot.getSnapshotForRange(requirement.getChunkRange());
            return snapshot.canHandleInput(requirement) ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(requirement));
        }


        @Override
        public void handle(RequirementWillMultiChunk requirement) {
            EnumDemonWillType type = requirement.willType;
            double remaining = requirement.getAmount();
            double minPerChunk = requirement.getMinPerChunk();

            for (Chunk chunk : chunksReader.getSurroundingChunks(this.world, this.pos, requirement.getChunkRange())) {
                if (remaining <= 0.0D) break;

                BlockPos blockPos = getBlockInChunk(chunk);
                double currentAmount = WorldDemonWillHandler.getCurrentWill(this.world, blockPos, type);
                double availableToDrain = Math.max(0.0D, currentAmount - minPerChunk);

                if (availableToDrain <= 0.0D) continue;

                double toDrain = Math.min(availableToDrain, remaining);
                WorldDemonWillHandler.drainWill(this.world, blockPos, type, toDrain, true);
                remaining -= toDrain;
            }

            updateSnapshot();
            refreshScheduler.recordSuccess();
        }
    }

    public static class Output extends TileWillMultiChunkProvider {
        @Nullable
        @Override
        public MachineComponentWillMultiChunkProvider provideComponent() {
            return new MachineComponentWillMultiChunkProvider(IOType.OUTPUT, this) {
            };
        }

        @Override
        protected CraftCheck checkSnapshot(RequirementWillMultiChunk requirement) {
            if (requirement.getChunkRange() > demonWillSnapshot.getCoveredRange()) {
                largestChunkRange = requirement.getChunkRange();
                return CraftCheck.failure(getKeyForRequirement(requirement));
            }

            DemonWillSnapshot snapshot = demonWillSnapshot.getSnapshotForRange(requirement.getChunkRange());
            return snapshot.canHandleOutput(requirement) ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(requirement));
        }

        @Override
        public void handle(RequirementWillMultiChunk requirement) {
            EnumDemonWillType type = requirement.willType;
            double totalToFill = requirement.getAmount();
            double maxPerChunk = requirement.getMaxPerChunk();

            List<Chunk> chunks = chunksReader.getSurroundingChunks(this.world, this.pos, requirement.getChunkRange());

            // Water-filling: sort chunks by current amount ascending so we raise the lowest first
            chunks.sort(Comparator.comparingDouble(chunk -> WorldDemonWillHandler.getCurrentWill(this.world, getBlockInChunk(chunk), type)));

            double remaining = totalToFill;
            int n = chunks.size();

            for (int i = 0; i < n; i++) {
                if (remaining <= 0.0D) break;

                double currentLevel = WorldDemonWillHandler.getCurrentWill(this.world, getBlockInChunk(chunks.get(i)), type);
                double nextLevel = (i + 1 < n) ? WorldDemonWillHandler.getCurrentWill(this.world, getBlockInChunk(chunks.get(i + 1)), type) : maxPerChunk;
                double targetLevel = Math.min(nextLevel, maxPerChunk);
                double levelIncrease = targetLevel - currentLevel;

                if (levelIncrease <= 0.0D) continue;

                int activeBuckets = i + 1;
                double needed = levelIncrease * activeBuckets;

                if (remaining >= needed) {
                    // Fill all active buckets up to the next level
                    for (int j = 0; j <= i; j++) {
                        BlockPos blockPos = getBlockInChunk(chunks.get(j));
                        WorldDemonWillHandler.fillWillToMaximum(this.world, blockPos, type, levelIncrease, maxPerChunk, true);
                    }
                    remaining -= needed;
                } else {
                    // Distribute remaining evenly across active buckets
                    double partialIncrease = remaining / activeBuckets;
                    for (int j = 0; j <= i; j++) {
                        BlockPos blockPos = getBlockInChunk(chunks.get(j));
                        WorldDemonWillHandler.fillWillToMaximum(this.world, blockPos, type, partialIncrease, maxPerChunk, true);
                    }
                    remaining = 0.0D;
                }
            }

            updateSnapshot();
            refreshScheduler.recordSuccess();
        }
    }
}
