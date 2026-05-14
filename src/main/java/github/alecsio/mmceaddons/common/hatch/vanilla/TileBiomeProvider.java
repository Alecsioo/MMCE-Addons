package github.alecsio.mmceaddons.common.hatch.vanilla;

import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;

import javax.annotation.Nullable;
import java.util.Objects;

public class TileBiomeProvider extends TileColorableMachineComponent implements MachineComponentTile, IRequirementHandler<RequirementBiome> {

    @Override
    public CraftCheck canHandle(RequirementBiome requirement) {
        return Objects.requireNonNull(this.world.getBiome(this.getPos()).getRegistryName()).toString().equalsIgnoreCase(requirement.getBiome().getRegistryName()) ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.biome") ;
    }

    @Override
    public void handle(RequirementBiome requirement) {
        // *eats the biome*
    }

    @Nullable
    @Override
    public MachineComponentBiomeProvider provideComponent() {
        return new MachineComponentBiomeProvider(IOType.INPUT, this);
    }
}
