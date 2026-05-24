package github.alecsio.mmceaddons.common.hatch.thaumcraft.vis;

import github.alecsio.mmceaddons.common.hatch.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.hatch.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.hatch.wrapper.AuraHelperWrapper;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileVisProvider extends AbstractMultiChunkHandler<RequirementVis> implements MachineComponentTile {

    @Override
    protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos blockPosInChunk) {
        return AuraHelperWrapper.Vis.getVis(this.world, blockPosInChunk);
    }

    @Override
    protected void updateSnapshot() {

    }

    @Override
    protected CraftCheck checkSnapshot(RequirementVis requirement) {
        return null;
    }

    public static class Input extends TileVisProvider {

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
            AuraHelperWrapper.Vis.drainVis(this.world, blockPosInChunk, (float) amountToHandle);
        }

        @Nullable
        @Override
        public MachineComponentVisProvider provideComponent() {
            return new MachineComponentVisProvider(IOType.INPUT, this);
        }
    }

    public static class Output extends TileVisProvider {

        public static final float MAXIMUM_AMOUNT_IN_CHUNK = 32766.0F; // This is a magic number imposed by the TC API

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
            AuraHelperWrapper.Vis.addVis(this.world, blockPosInChunk, (float) amountToHandle);
        }

        @Nullable
        @Override
        public MachineComponentVisProvider provideComponent() {
            return new MachineComponentVisProvider(IOType.OUTPUT, this);
        }
    }

}
