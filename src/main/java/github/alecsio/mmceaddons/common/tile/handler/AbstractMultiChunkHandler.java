package github.alecsio.mmceaddons.common.tile.handler;

import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.tile.handler.chunks.ChunksReader;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.minecraft.util.math.MathHelper.clamp;

public abstract class AbstractMultiChunkHandler<T extends IMultiChunkRequirement> extends TileColorableMachineComponent implements IRequirementHandler<T> {

    protected final ChunksReader chunksReader = ChunksReader.getInstance();

    public CraftCheck canHandle(T requirement) {
        return requirement.getIOType() == IOType.INPUT ? canHandleInput(requirement) : canHandleOutput(requirement);
    }

    @Override
    public void handle(T requirement) {
        if (requirement.getIOType() == IOType.INPUT) {
            handleInputForChunks(requirement, false);
        } else {
            handleOutputForChunks(requirement, false);
        }
    }

    private static List<Chunk> validateNotNullAndLoaded(List<Chunk> chunks) {
        return chunks.stream()
                .filter(Objects::nonNull)
                .filter(Chunk::isLoaded)
                .collect(Collectors.toList());
    }

    private CraftCheck canHandleInput(T requirement) {
        double totalHandled = handleInputForChunks(requirement, true);

        return totalHandled == requirement.getAmount() ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(requirement));
    }

    private CraftCheck canHandleOutput(T requirement) {
        double totalAmount = requirement.getAmount();
        double totalHandled = handleOutputForChunks(requirement, true);

        return clampWithEpsilon(totalAmount - totalHandled, 0.0, 1.0, 1e-6) == 0 ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(requirement));
    }

    private boolean areChunksInvalid(List<Chunk> chunks) {
        int totalChunksBeforeValidations = chunks.size();
        chunks = validateNotNullAndLoaded(chunks);
        return chunks.isEmpty() || chunks.size() != totalChunksBeforeValidations;
    }

    private double handleInputForChunks(T requirement, boolean simulate) {
        List<Chunk> chunks = chunksReader.getSurroundingChunks(world, this.pos, requirement.getChunkRange());
        if (areChunksInvalid(chunks)) return 0;

        List<Integer> indexes = IntStream.range(0, chunks.size()).boxed().collect(Collectors.toList());
        Collections.shuffle(indexes);

        double totalAmount = requirement.getAmount();
        double totalHandled = 0;

        for (Integer index : indexes) {
            Chunk chunk = chunks.get(index);
            BlockPos pos = getBlockInChunk(chunk);
            double handled = handleInputForChunk(requirement, pos, totalAmount - totalHandled, simulate);
            totalHandled += handled;
            if (totalHandled == totalAmount) {
                break;
            }
        }

        // Due to floating point rounding we could end up with results extremely close to the total amount. In those cases,
        // I just round up to reach the total amount (ie 999.999999 -> 1000.0
        return clampWithEpsilon(totalAmount - totalHandled, 0.0, totalAmount, 1e-6) == 0 ? totalAmount : totalHandled;
    }

    private double handleOutputForChunks(T requirement, boolean simulate) {
        List<Chunk> chunks = chunksReader.getSurroundingChunks(world, this.pos, requirement.getChunkRange());
        if (areChunksInvalid(chunks)) return 0;

        double totalAmount = requirement.getAmount();
        double amountPerChunk = totalAmount / chunks.size();
        double totalHandled = 0;

        for (Chunk chunk : chunks) {
            BlockPos pos = getBlockInChunk(chunk);
            double handled = handleOutputForChunk(requirement, pos, amountPerChunk, simulate);
            totalHandled += handled;
        }

        return totalHandled;
    }

    private double handleInputForChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos, double amountToHandle, boolean simulate) {
        double currentAmount = getAmountInChunk(requirement, randomBlockPos);
        // Check if the chunk can handle this amount
        if (currentAmount - amountToHandle >= requirement.getMinPerChunk()) {
            if (!simulate) {
                // Perform the handling
                handleAmount(requirement, randomBlockPos, amountToHandle);
            }
            return amountToHandle; // Fully handled
        }

        // Can't handle the full amount -> figure out how much it can handle
        double possibleAmount = Math.max(0, currentAmount - requirement.getMinPerChunk());
        if (possibleAmount > 0) {
            if (!simulate) {
                handleAmount(requirement, randomBlockPos, possibleAmount);
            }
            return possibleAmount; // Partially handled
        }

        return 0; // Nothing handled
    }

    private double handleOutputForChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos, double amountToHandle, boolean simulate) {
        double currentAmount = getAmountInChunk(requirement, randomBlockPos);
        // Calculate how much space is left
        double availableSpace = requirement.getMaxPerChunk() - currentAmount;

        // Nothing can be output if we're already at or above the max
        if (availableSpace <= 0) {
            return 0;
        }

        // Determine how much we can actually output
        double amountToOutput = Math.min(amountToHandle, availableSpace);

        if (!simulate) {
            handleAmount(requirement, randomBlockPos, amountToOutput);
        }

        return amountToOutput; // Amount successfully output
    }

    private String getKeyForRequirement(T requirement) {
        return requirement.getIOType().equals(IOType.INPUT) ? "error.modularmachineryaddons.requirement.missing.multichunk.input" : "error.modularmachineryaddons.requirement.missing.multichunk.output";
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
    abstract protected void handleAmount(IMultiChunkRequirement requirement, BlockPos randomBlockPos, double amountToHandle);
}

