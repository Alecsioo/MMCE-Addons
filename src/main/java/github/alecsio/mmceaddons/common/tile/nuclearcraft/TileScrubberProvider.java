package github.alecsio.mmceaddons.common.tile.nuclearcraft;

import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.crafting.requirement.nuclearcraft.RequirementScrubber;
import github.alecsio.mmceaddons.common.tile.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentScrubberProvider;
import github.alecsio.mmceaddons.common.tile.wrapper.RadiationHelperWrapper;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TileScrubberProvider extends AbstractMultiChunkHandler<RequirementScrubber> implements MachineComponentTile {

    private int currentChunkRange = 0;
    // Not persisted on purpose, since the cache is not persisted either, it needs a refresh if there's a restart
    private AtomicBoolean marked = new AtomicBoolean(false);
    private AtomicBoolean handledRedstone = new AtomicBoolean(false);

    @Override
    protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos) {
        return RadiationHelperWrapper.getRadiationAmount(this.world.getChunk(randomBlockPos));
    }

    @Override
    protected boolean canChunkHandle(double currentAmount, double amountToModify, IMultiChunkRequirement requirement) {
        return true;
    }

    @Override
    public void handle(RequirementScrubber requirement) {
        handledRedstone.set(true);

        if (currentChunkRange != requirement.getChunkRange()) {
            unmarkAllChunksAsScrubbed();
            currentChunkRange = requirement.getChunkRange();
        }
        if (marked.get()) {return;}

        Pair<Integer, List<Chunk>> dimChunkList = getSurroundingChunks(world, this.pos, requirement.getChunkRange());
        List<Chunk> chunks = dimChunkList.getValue();
        int dimensionId = dimChunkList.getKey();

        chunks.forEach(chunk -> ScrubbedChunksCache.markAsScrubbed(dimensionId, chunk.getPos()));

        marked.set(true);
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

    public void unmarkAllChunksAsScrubbed() {
        if (handledRedstone.get()) {return;}
        Pair<Integer, List<Chunk>> dimChunkList = getSurroundingChunks(world, this.pos, currentChunkRange);
        List<Chunk> chunks = dimChunkList.getValue();
        int dimensionId = dimChunkList.getKey();

        ScrubbedChunksCache.unmarkAllAsScrubbed(dimensionId, chunks.stream().map(Chunk::getPos).collect(Collectors.toList()));
        marked.set(false);
    }

    public void setHandledRedstone() {
        this.handledRedstone.set(true);
    }

    private Pair<Integer, List<Chunk>> getSurroundingChunks(World world, BlockPos pos, int range) {
        return Pair.of(world.provider.getDimension(), chunksReader.getSurroundingChunks(world, pos, range));
    }
}
