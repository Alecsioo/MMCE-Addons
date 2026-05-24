package github.alecsio.mmceaddons.common.hatch.thaumcraft.flux;

import github.alecsio.mmceaddons.common.hatch.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.exception.ConsistencyException;
import github.alecsio.mmceaddons.common.hatch.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.wrapper.AuraHelperWrapper;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileFluxProvider extends AbstractMultiChunkHandler<RequirementFlux> implements MachineComponentTile {

    @Override
    protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos blockPosInChunk) {
        return AuraHelperWrapper.Flux.getFlux(this.world, blockPosInChunk);
    }

    protected float castWithSafeguard(double amount) {
        if (amount > Float.MAX_VALUE) {
            throw new ConsistencyException("This should never happen");
        }
        return (float) amount;
    }

    @Override
    protected void updateSnapshot() {

    }

    @Override
    protected CraftCheck checkSnapshot(RequirementFlux requirement) {
        return null;
    }

    public static class Input extends TileFluxProvider {
        @Nullable
        @Override
        public MachineComponent<IRequirementHandler<RequirementFlux>> provideComponent() {
            return new MachineComponentFluxProvider(this, IOType.INPUT);
        }

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
            AuraHelperWrapper.Flux.drainFlux(this.world, blockPosInChunk, castWithSafeguard(amountToHandle));
        }

    }

    public static class Output extends TileFluxProvider {

        public static final float MAXIMUM_AMOUNT_IN_CHUNK = 32766.0F; // This is a magic number imposed by the TC API

        @Nullable
        @Override
        public MachineComponentFluxProvider provideComponent() {
            return new MachineComponentFluxProvider(this, IOType.OUTPUT);
        }

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
            AuraHelperWrapper.Flux.addFlux(this.world, blockPosInChunk, castWithSafeguard(amountToHandle));
        }
    }
}
