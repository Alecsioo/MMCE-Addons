package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import github.alecsio.mmceaddons.common.hatch.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.hatch.wrapper.WillHelperWrapper;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileWillMultiChunkProvider extends AbstractMultiChunkHandler<RequirementWillMultiChunk> implements MachineComponentTile {

    @Override
    protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos) {
        return WillHelperWrapper.getWill(this.world, randomBlockPos, ((RequirementWillMultiChunk)requirement).willType);
    }

    public static class Input extends TileWillMultiChunkProvider {
        @Nullable
        @Override
        public MachineComponentWillMultiChunkProvider provideComponent() {
            return new MachineComponentWillMultiChunkProvider(IOType.INPUT, this) {};
        }

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos randomBlockPos, double amountToHandle) {
            WillHelperWrapper.drainWill(this.world, randomBlockPos, ((RequirementWillMultiChunk)requirement).willType, amountToHandle);
        }

    }

    public static class Output extends TileWillMultiChunkProvider {
        @Nullable
        @Override
        public MachineComponentWillMultiChunkProvider provideComponent() {
            return new MachineComponentWillMultiChunkProvider(IOType.OUTPUT, this) {};
        }

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos randomBlockPos, double amountToHandle) {
            WillHelperWrapper.addWill(this.world, randomBlockPos, ((RequirementWillMultiChunk)requirement).willType, amountToHandle);
        }

    }
}
