package github.alecsio.mmceaddons.common.tile.handler;

import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.tile.handler.chunks.ChunksReader;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static net.minecraft.util.math.MathHelper.clamp;

public abstract class AbstractMultiChunkHandler<T extends IMultiChunkRequirement> extends TileColorableMachineComponent implements IRequirementHandler<T> {

    private final ChunksReader chunksReader = ChunksReader.getInstance();

    public CraftCheck canHandle(T requirement) {
        List<Chunk> chunks = chunksReader.getSurroundingChunks(world, this.pos, requirement.getChunkRange());
        int totalChunksBeforeValidations = chunks.size();
        chunks = validateNotNullAndLoaded(chunks);
        if (chunks.isEmpty() || chunks.size() != totalChunksBeforeValidations) return CraftCheck.failure(I18n.format("error.modularmachineryaddons.requirement.missing.multichunk", requirement.getIOType().toString().toLowerCase(), getRequirementName(requirement), requirement.getChunkRange()));

        double totalAmount = requirement.getAmount();
        double amountPerChunk = totalAmount / chunks.size();
        double totalHandled = 0;

        for (Chunk chunk : chunks) {
            BlockPos pos = getBlockInChunk(chunk);
            double amountInChunk = getAmountInChunk(requirement, pos);

            if (!canChunkHandle(amountInChunk, totalAmount - totalHandled, requirement)) break;

            totalHandled += amountPerChunk;
        }

        return clampWithEpsilon(totalAmount - totalHandled, 0.0, 1.0, 1e-6) == 0 ? CraftCheck.success() : CraftCheck.failure(I18n.format("error.modularmachineryaddons.requirement.missing.multichunk", requirement.getIOType().toString().toLowerCase(), getRequirementName(requirement), requirement.getChunkRange()));
    }

    @Override
    public void handle(T requirement) {
        List<Chunk> chunks = validateNotNullAndLoaded(chunksReader.getSurroundingChunks(world, this.pos, requirement.getChunkRange()));
        if (chunks.isEmpty()) return;

        double totalAmount = requirement.getAmount();
        double amountPerChunk = totalAmount / chunks.size();

        chunks.forEach(chunk -> {
            BlockPos pos = getBlockInChunk(chunk);
            handleAmount(requirement, pos, amountPerChunk);
        });
    }

    private static List<Chunk> validateNotNullAndLoaded(List<Chunk> chunks) {
        return chunks.stream()
                .filter(Objects::nonNull)
                .filter(Chunk::isLoaded)
                .collect(Collectors.toList());
    }

    private String getRequirementName(T requirement) {
        return requirement.getClass().getSimpleName().replace("Requirement", "");
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

