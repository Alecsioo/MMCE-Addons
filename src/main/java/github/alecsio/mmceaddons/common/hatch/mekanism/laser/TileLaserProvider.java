package github.alecsio.mmceaddons.common.hatch.mekanism.laser;

import com.google.common.util.concurrent.AtomicDouble;
import github.alecsio.mmceaddons.common.MMCEAConfig;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import mekanism.api.lasers.ILaserReceptor;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TileLaserProvider extends TileColorableMachineComponent implements MachineComponentTile, IAsyncRequirementHandler<RequirementLaser>, ILaserReceptor {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final AtomicDouble storedEnergy = new AtomicDouble(0);

    public double getStoredEnergy() {
        return storedEnergy.get();
    }

    @Override
    public CraftCheck canHandleSync(RequirementLaser requirement) {
        lock.readLock().lock();
        try {
            return storedEnergy.doubleValue() >= requirement.getPower() ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.laser.input");
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public CraftCheck canHandleAsync(RequirementLaser requirement) {
        return canHandleSync(requirement);
    }

    @Override
    public void handle(RequirementLaser requirement) {
        double energy = requirement.getPower();
        double newEnergy = Math.max(0, storedEnergy.get() - energy);
        lock.writeLock().lock();
        try {
            storedEnergy.set(newEnergy);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Nullable
    @Override
    public MachineComponent<?> provideComponent() {
        return new MachineComponentLaserProvider(IOType.INPUT, this);
    }

    @Override
    public void receiveLaserEnergy(double v, EnumFacing enumFacing) {
        double currentEnergy = storedEnergy.get();
        double newEnergy = Math.min(currentEnergy + v, MMCEAConfig.laserHatchesCapacity);
        storedEnergy.set(newEnergy);
    }

    @Override
    public boolean canLasersDig() {
        return false;
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        this.storedEnergy.set(compound.getDouble("storedEnergy"));
        super.readCustomNBT(compound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setDouble("storedEnergy", this.storedEnergy.get());
        super.writeCustomNBT(compound);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        return capability == Capabilities.LASER_RECEPTOR_CAPABILITY || super.hasCapability(capability, side);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        if (capability == Capabilities.LASER_RECEPTOR_CAPABILITY) {
            return Capabilities.LASER_RECEPTOR_CAPABILITY.cast(this);
        }
        return super.getCapability(capability, side);
    }
}
