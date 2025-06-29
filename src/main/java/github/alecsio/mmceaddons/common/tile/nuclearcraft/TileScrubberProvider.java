package github.alecsio.mmceaddons.common.tile.nuclearcraft;

import github.alecsio.mmceaddons.common.cache.InterDimensionalBlockPos;
import github.alecsio.mmceaddons.common.cache.ScrubbedChunksCache;
import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.crafting.requirement.nuclearcraft.RequirementScrubber;
import github.alecsio.mmceaddons.common.tile.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentScrubberProvider;
import github.alecsio.mmceaddons.common.tile.wrapper.RadiationHelperWrapper;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class TileScrubberProvider extends AbstractMultiChunkHandler<RequirementScrubber> implements MachineComponentTile {

    private int currentChunkRange = 0;
    private InterDimensionalBlockPos interDimensionalBlockPos;

    public TileScrubberProvider() {
        if (world != null) {
            this.interDimensionalBlockPos = new InterDimensionalBlockPos(world.provider.getDimension(), this.pos);
        }
    }

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
        if (currentChunkRange != requirement.getChunkRange()) {
            unmarkAllChunksAsScrubbed();
            currentChunkRange = requirement.getChunkRange();
        }
        setInterDimensionalBlockPosIfNecessary();
        if (ScrubbedChunksCache.isScrubbed(interDimensionalBlockPos)) {return;}

        markAllChunksAsScrubbed();
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
        setInterDimensionalBlockPosIfNecessary();
        List<ChunkPos> scrubbedChunks = getSurroundingChunks(world, this.pos, currentChunkRange);
        ScrubbedChunksCache.unmarkAsScrubbed(interDimensionalBlockPos, scrubbedChunks);
    }

    public void markAllChunksAsScrubbed() {
        setInterDimensionalBlockPosIfNecessary();
        List<ChunkPos> chunks = getSurroundingChunks(world, this.pos, currentChunkRange);
        ScrubbedChunksCache.markAsScrubbed(interDimensionalBlockPos, chunks);
    }

    private List<ChunkPos> getSurroundingChunks(World world, BlockPos pos, int range) {
        return chunksReader.getSurroundingChunks(world, pos, range).stream().map(Chunk::getPos).collect(Collectors.toList());
    }

    private void setInterDimensionalBlockPosIfNecessary() {
        if (interDimensionalBlockPos == null && world != null) {
            interDimensionalBlockPos = new InterDimensionalBlockPos(world.provider.getDimension(), this.pos);
        }
    }
}
