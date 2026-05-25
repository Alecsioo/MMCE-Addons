package github.alecsio.mmceaddons.common.hatch.vanilla;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class TileBiomeProvider extends AbstractSnapshotMachineComponent<RequirementBiome> implements MachineComponentTile {

    private String biomeRegistryName;

    @Override
    public void handle(RequirementBiome requirement) {
        // *eats the biome*
        super.handle(requirement);
    }

    @Nullable
    @Override
    public MachineComponentBiomeProvider provideComponent() {
        return new MachineComponentBiomeProvider(IOType.INPUT, this);
    }

    @Override
    protected void updateSnapshot() {
        ResourceLocation biomeRegistryName = this.world.getBiome(this.getPos()).getRegistryName();
        lock.writeLock().lock();
        try {
            this.biomeRegistryName = biomeRegistryName != null ? biomeRegistryName.toString() : this.biomeRegistryName;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected CraftCheck checkSnapshot(RequirementBiome requirement) {
        return biomeRegistryName.equalsIgnoreCase(requirement.getBiome().getRegistryName()) ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.biome");
    }
}
