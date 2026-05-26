package github.alecsio.mmceaddons.common.hatch.abyssalcraft;

import com.google.common.util.concurrent.AtomicDouble;
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

    protected AtomicDouble storedEnergy = new AtomicDouble(0);

    @Override
    public float getContainedEnergy() {
        return storedEnergy.floatValue();
    }

    @Override
    public int getMaxEnergy() {
        return MMCEAConfig.potentialEnergyHatchesCapacity;
    }

    @Override
    public float consumeEnergy(float energy) {
        float localEnergy = storedEnergy.floatValue();
        if (energy < localEnergy) {
            storedEnergy.set(localEnergy - energy);
            return energy;
        } else {
            storedEnergy.set(0);
            return localEnergy;
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
        this.storedEnergy.addAndGet(v);
    }

    @Override
    public boolean canAcceptPE() {
        return storedEnergy.doubleValue() < getMaxEnergy();
    }

    @Override
    public boolean canTransferPE() {
        return storedEnergy.doubleValue() > 0;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        storedEnergy.set(compound.getFloat("storedEnergy"));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setFloat("storedEnergy", storedEnergy.floatValue());
    }

    public static class Input extends TilePotentialEnergyProvider {
        @Override
        public CraftCheck canHandle(RequirementPotentialEnergy requirement) {
            return storedEnergy.doubleValue() - requirement.getAmount() >= 0 ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.potentialenergy.input");
        }

        @Override
        public void handle(RequirementPotentialEnergy requirement) {
            storedEnergy.set(storedEnergy.doubleValue() - requirement.getAmount());
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
            return (storedEnergy.doubleValue() + requirement.getAmount()) <= MMCEAConfig.potentialEnergyHatchesCapacity ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.potentialenergy.output");
        }

        @Override
        public void handle(RequirementPotentialEnergy requirement) {
            storedEnergy.set(storedEnergy.doubleValue() + requirement.getAmount());
        }

        @Nullable
        @Override
        public MachineComponent<?> provideComponent() {
            return new MachineComponentPotentialEnergyProvider(IOType.OUTPUT, this);
        }
    }
}
