package github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import github.alecsio.mmceaddons.common.hatch.handler.chunks.ChunksReader;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache.InterdimensionalChunkPos;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.cache.ScrubbedChunksCache;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.event.MachineControllerInvalidatedEvent;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.event.MachineControllerRedstoneAffectedEvent;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.event.MachineNotFormedEvent;
import github.kasuminova.mmce.common.event.machine.MachineEvent;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTileNotifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TileScrubberProvider extends AbstractSnapshotMachineComponent<RequirementScrubber> implements MachineComponentTileNotifiable {

    private int currentChunkRange = -1;
    protected final ChunksReader chunksReader = ChunksReader.getInstance();
    // Not persisted on purpose, since the cache is not persisted either, it needs a refresh if there's a restart
    private final AtomicBoolean redstonePowered = new AtomicBoolean(false);
    private List<InterdimensionalChunkPos> scrubbedChunks = Collections.synchronizedList(new ArrayList<>());

    @Override
    protected CraftCheck checkSnapshot(RequirementScrubber requirement) {
        return CraftCheck.success();
    }

    @Override
    protected void updateSnapshot() {

    }

    @Override
    public void handle(RequirementScrubber requirement) {
        if (currentChunkRange != requirement.getChunkRange() || scrubbedChunks.isEmpty()) {
            replaceScrubbedChunks(requirement.getChunkRange());
        }
    }

    @Nullable
    @Override
    public MachineComponentScrubberProvider provideComponent() {
        return new MachineComponentScrubberProvider(IOType.INPUT, this);
    }

    // Some of these impl might still suffer from concurrency problems but it might be acceptable
    public void onRedstoneSignal(boolean powered) {
        redstonePowered.set(powered);
        if (powered)  {
            clearScrubbedChunks();
        } else {
            replaceScrubbedChunks(currentChunkRange);
        }
    }

    public void clearScrubbedChunks() {
        ScrubbedChunksCache.removeScrubbedChunks(scrubbedChunks);
        scrubbedChunks.clear();
    }

    private void replaceScrubbedChunks(int newChunkRange) {
        if ((newChunkRange == currentChunkRange && !scrubbedChunks.isEmpty()) || redstonePowered.get()) {
            return;
        }

        clearScrubbedChunks();
        currentChunkRange = newChunkRange;
        List<Long> surroundingChunks = getSurroundingChunks(world, this.pos, currentChunkRange);
        scrubbedChunks = surroundingChunks.stream().map(chunkPosAsLong -> InterdimensionalChunkPos.of(world.provider.getDimension(), chunkPosAsLong)).collect(Collectors.toList());
        ScrubbedChunksCache.addChunksToCache(scrubbedChunks);
    }

    @Override
    public void invalidate() {
        ScrubbedChunksCache.removeScrubbedChunks(scrubbedChunks);
        scrubbedChunks.clear();
        super.invalidate();
    }

    private List<Long> getSurroundingChunks(World world, BlockPos pos, int range) {
        return chunksReader.getSurroundingChunks(world, pos, range).stream()
                .map(Chunk::getPos)
                .map(chunkPos -> ChunkPos.asLong(chunkPos.x, chunkPos.z))
                .collect(Collectors.toList());
    }

    @Override
    public void onMachineEvent(MachineEvent event) {
        if (event instanceof MachineNotFormedEvent || event instanceof MachineControllerInvalidatedEvent) {
            clearScrubbedChunks();
        }

        if (event instanceof MachineControllerRedstoneAffectedEvent machineControllerRedstoneAffectedEvent) {
            onRedstoneSignal(machineControllerRedstoneAffectedEvent.isPowered);
        }
    }
}
