package github.alecsio.mmceaddons.common.tile.nuclearcraft;

import github.alecsio.mmceaddons.common.cache.InterdimensionalChunkPos;
import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.crafting.requirement.nuclearcraft.RequirementScrubber;
import github.alecsio.mmceaddons.common.event.MachineControllerInvalidatedEvent;
import github.alecsio.mmceaddons.common.event.MachineControllerRedstoneAffectedEvent;
import github.alecsio.mmceaddons.common.event.MachineNotFormedEvent;
import github.alecsio.mmceaddons.common.tile.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentScrubberProvider;
import github.alecsio.mmceaddons.common.tile.wrapper.RadiationHelperWrapper;
import github.kasuminova.mmce.common.event.machine.MachineEvent;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTileNotifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class TileScrubberProvider extends AbstractMultiChunkHandler<RequirementScrubber> implements MachineComponentTileNotifiable {

    private static final Logger log = LogManager.getLogger(TileScrubberProvider.class);
    private int currentChunkRange = -1;
    // Not persisted on purpose, since the cache is not persisted either, it needs a refresh if there's a restart
    private final AtomicBoolean redstonePowered = new AtomicBoolean(false);
    private final AtomicBoolean needsRefresh = new AtomicBoolean(false);
    private List<Long> scrubbedChunks = new ArrayList<>();
    private final Lock scrubbedChunksLock = new ReentrantLock();

    @Override
    protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos) {
        return RadiationHelperWrapper.getRadiationAmount(this.world.getChunk(randomBlockPos));
    }

    @Override
    protected boolean canChunkHandle(double currentAmount, double amountToModify, IMultiChunkRequirement requirement) {
        return currentChunkRange == requirement.getChunkRange();
    }

    @Override
    public void handle(RequirementScrubber requirement) {
        if (currentChunkRange != requirement.getChunkRange() || needsRefresh.compareAndSet(true, false)) {
            log.info("Current chunk range for scrubber at {} is {}. New chunk range: {}", this.pos.toString(), currentChunkRange, requirement.getChunkRange());
            replaceScrubbedChunks(requirement.getChunkRange());
        }
    }

    @Override
    protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
        RadiationHelperWrapper.scrubRadiation(world.getChunk(blockPosInChunk));
    }

    @Nullable
    @Override
    public MachineComponentScrubberProvider provideComponent() {
        return new MachineComponentScrubberProvider(IOType.INPUT, this);
    }

    // Some of these impl might still suffer from concurrency problems but it might be acceptable
    public void onRedstoneSignal(boolean powered) {
        redstonePowered.set(powered);
        if (!powered) {
            needsRefresh.set(true);
        }
    }

    public boolean canScrubChunk(long chunkPosAsLong) {
        return !isRedstonePowered() && isChunkScrubbed(chunkPosAsLong);
    }

    public boolean isRedstonePowered() {
        return redstonePowered.get();
    }

    public boolean isChunkScrubbed(long chunkPosAsLong) {
        return scrubbedChunks.contains(chunkPosAsLong);
    }

    public void clearScrubbedChunks() {
        scrubbedChunksLock.lock();
        try {
            scrubbedChunks.clear();
        } finally {
            scrubbedChunksLock.unlock();
        }
        needsRefresh.set(true);
    }

    private void replaceScrubbedChunks(int newChunkRange) {
        scrubbedChunksLock.lock();
        try {
            currentChunkRange = newChunkRange;
            scrubbedChunks = getSurroundingChunks(world, this.pos, currentChunkRange);
            List<InterdimensionalChunkPos> interdimensionalChunkPos = scrubbedChunks.stream().map(chunkPosAsLong -> InterdimensionalChunkPos.of(world.provider.getDimension(), chunkPosAsLong)).collect(Collectors.toList());
            log.debug("Adding {} chunks to the scrubber cache from scrubber at {}", interdimensionalChunkPos.size(), this.pos.toString());
            ScrubbedChunksCache.addChunksToCache(interdimensionalChunkPos, this.pos);
        } finally {
            scrubbedChunksLock.unlock();
        }
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
            log.info("Received event {} from controller. Clearing scrubber chunk cache at {}", event.getClass().getSimpleName(), pos.toString());
            clearScrubbedChunks();
        }

        if (event instanceof MachineControllerRedstoneAffectedEvent machineControllerRedstoneAffectedEvent) {
            onRedstoneSignal(machineControllerRedstoneAffectedEvent.isPowered);
        }
    }
}
