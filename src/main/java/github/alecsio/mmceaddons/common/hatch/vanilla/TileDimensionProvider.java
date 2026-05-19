package github.alecsio.mmceaddons.common.hatch.vanilla;

import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;

import javax.annotation.Nullable;

public class TileDimensionProvider extends TileColorableMachineComponent implements MachineComponentTile, IRequirementHandler<RequirementDimension> {

    @Override
    public CraftCheck canHandle(RequirementDimension requirement) {
        return this.world.provider.getDimension() == requirement.getDimension().getId() ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.dimension");
    }

    @Override
    public void handle(RequirementDimension requirement) {
        // *eats the dimension*
    }

    @Nullable
    @Override
    public MachineComponentDimensionProvider provideComponent() {
        return new MachineComponentDimensionProvider(IOType.INPUT, this);
    }
}
