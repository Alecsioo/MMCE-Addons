package github.alecsio.mmceaddons.common.hatch.vanilla;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;

import javax.annotation.Nullable;

public class TileDimensionProvider extends AbstractSnapshotMachineComponent<RequirementDimension> implements MachineComponentTile, IRequirementHandler<RequirementDimension> {

    private int dimensionId;

    @Override
    protected void updateSnapshot() {
        lock.writeLock().lock();
        try {
            this.dimensionId = this.world.provider.getDimension();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected CraftCheck checkSnapshot(RequirementDimension requirement) {
        return this.dimensionId == requirement.getDimension().getId() ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.dimension");
    }

    @Override
    public void handle(RequirementDimension requirement) {
        // *eats the dimension*
        super.handle(requirement);
    }

    @Nullable
    @Override
    public MachineComponentDimensionProvider provideComponent() {
        return new MachineComponentDimensionProvider(IOType.INPUT, this);
    }
}
