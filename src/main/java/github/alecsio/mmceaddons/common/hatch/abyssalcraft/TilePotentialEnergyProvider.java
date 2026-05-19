package github.alecsio.mmceaddons.common.hatch.abyssalcraft;

import com.shinoow.abyssalcraft.api.energy.IEnergyCollector;
import com.shinoow.abyssalcraft.api.energy.IEnergyTransporter;
import github.alecsio.mmceaddons.common.MMCEAConfig;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nullable;

public abstract class TilePotentialEnergyProvider extends TileColorableMachineComponent implements MachineComponentTile, IRequirementHandler<RequirementPotentialEnergy>, IEnergyCollector, IEnergyTransporter {

    protected float storedEnergy;

    @Override
    public float getContainedEnergy() {
        return storedEnergy;
    }

    @Override
    public int getMaxEnergy() {
        return MMCEAConfig.potentialEnergyHatchesCapacity;
    }

    @Override
    public float consumeEnergy(float energy) {
        if(energy < this.storedEnergy){
            this.storedEnergy -= energy;
            return energy;
        } else {
            float ret = this.storedEnergy;
            this.storedEnergy = 0;
            return ret;
        }
    }

    @Override
    public TileEntity getContainerTile() {
        return this;
    }

    @Override
    public void transferPE(EnumFacing enumFacing, float v) {

    }

    @Override
    public void addEnergy(float v) {
        this.storedEnergy += v;
    }

    @Override
    public boolean canAcceptPE() {
        return storedEnergy < getMaxEnergy();
    }

    @Override
    public boolean canTransferPE() {
        return storedEnergy > 0;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        storedEnergy = compound.getFloat("storedEnergy");
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setFloat("storedEnergy", storedEnergy);
    }

    public static class Input extends TilePotentialEnergyProvider {
        @Override
        public CraftCheck canHandle(RequirementPotentialEnergy requirement) {
            return storedEnergy + requirement.getAmount() <= getMaxEnergy() ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.potentialenergy.input");
        }

        @Override
        public void handle(RequirementPotentialEnergy requirement) {
            storedEnergy += requirement.getAmount();
        }

        @Nullable
        @Override
        public MachineComponent<?> provideComponent() {
            return new MachineComponentPotentialEnergyProvider(IOType.INPUT, this);
        }
    }

    public static class Output extends TilePotentialEnergyProvider {
        @Override
        public CraftCheck canHandle(RequirementPotentialEnergy requirement) {
            return (storedEnergy - requirement.getAmount()) >= 0.0f ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.potentialenergy.output");
        }

        @Override
        public void handle(RequirementPotentialEnergy requirement) {
            storedEnergy -= requirement.getAmount();
        }

        @Nullable
        @Override
        public MachineComponent<?> provideComponent() {
            return new MachineComponentPotentialEnergyProvider(IOType.OUTPUT, this);
        }
    }
}
