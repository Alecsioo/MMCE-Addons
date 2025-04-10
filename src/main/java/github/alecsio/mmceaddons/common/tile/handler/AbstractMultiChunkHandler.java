package github.alecsio.mmceaddons.common.tile.handler;

import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.tile.handler.chunks.ChunksReader;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

import static net.minecraft.util.math.MathHelper.clamp;

public abstract class AbstractMultiChunkHandler<T extends IMultiChunkRequirement> extends TileColorableMachineComponent implements IRequirementHandler<T> {

    private final ChunksReader chunksReader = ChunksReader.getInstance();

    public CraftCheck canHandle(T requirement) {
        return handle(requirement, false) == 0 ? CraftCheck.success() : CraftCheck.failure(I18n.format("error.modularmachineryaddons.requirement.missing.multichunk", requirement.getClass().getSimpleName().replace("Requirement", "")));
    }

    @Override
    public void handle(T requirement) {
        handle(requirement, true);
    }

    public double handle(T requirement, boolean doAction) {
        List<Chunk> chunks = chunksReader.getSurroundingChunks(world, this.pos, requirement.getChunkRange());
        if (chunks.isEmpty()) return requirement.getAmount(); // No chunks, return full amount

        double totalAmount = requirement.getAmount();
        double amountPerChunk = totalAmount / chunks.size();
        double totalHandled = 0;

        for (Chunk chunk : chunks) {
            if (chunk == null || !chunk.isLoaded()) break;

            BlockPos pos = getBlockInChunk(chunk);
            double amountInChunk = getAmountInChunk(requirement, pos);

            if (!canChunkHandle(amountInChunk, totalAmount - totalHandled, requirement) && !doAction) break;

            if (doAction) {
                handleAmount(requirement, pos, amountPerChunk);
            }

            totalHandled += amountPerChunk;
        }



        return clampWithEpsilon(totalAmount - totalHandled, 0.0, 1.0, 1e-6);
    }

    public static double clampWithEpsilon(double value, double min, double max, double epsilon) {
        if (Math.abs(value) < epsilon) return 0.0;
        return clamp(value, min, max);
    }

    public BlockPos getBlockInChunk(Chunk chunk) {
        // Get chunk's starting block position
        int chunkStartX = chunk.getPos().x * 16;
        int chunkStartZ = chunk.getPos().z * 16;

        return new BlockPos(chunkStartX, 0, chunkStartZ);
    }

    abstract protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos);
    abstract protected boolean canChunkHandle(double currentAmount, double amountToModify, IMultiChunkRequirement requirement);
    abstract protected void handleAmount(IMultiChunkRequirement requirement, BlockPos randomBlockPos, double amountToHandle);
}

