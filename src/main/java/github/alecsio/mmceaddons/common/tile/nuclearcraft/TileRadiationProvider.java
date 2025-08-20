package github.alecsio.mmceaddons.common.tile.nuclearcraft;

import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.crafting.requirement.nuclearcraft.RequirementRadiation;
import github.alecsio.mmceaddons.common.tile.handler.AbstractMultiChunkHandler;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentRadiationProvider;
import github.alecsio.mmceaddons.common.tile.wrapper.RadiationHelperWrapper;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileRadiationProvider extends AbstractMultiChunkHandler<RequirementRadiation> implements MachineComponentTile {

    @Override
    protected double getAmountInChunk(IMultiChunkRequirement requirement, BlockPos randomBlockPos) {
        return RadiationHelperWrapper.getRadiationAmount(this.world.getChunk(randomBlockPos));
    }

    public static class Input extends TileRadiationProvider {
        @Nullable
        @Override
        public MachineComponent<IRequirementHandler<RequirementRadiation>> provideComponent() {
            return new MachineComponentRadiationProvider(IOType.INPUT, this);
        }

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
            RadiationHelperWrapper.decreaseRadiationLevel(world.getChunk(blockPosInChunk), amountToHandle);
        }
    }

    public static class Output extends TileRadiationProvider {
        @Nullable
        @Override
        public MachineComponent<IRequirementHandler<RequirementRadiation>> provideComponent() {
            return new MachineComponentRadiationProvider(IOType.OUTPUT, this);
        }

        @Override
        protected void handleAmount(IMultiChunkRequirement requirement, BlockPos blockPosInChunk, double amountToHandle) {
            RadiationHelperWrapper.increaseRadiationBuffer(world.getChunk(blockPosInChunk), amountToHandle);
        }
    }
}
