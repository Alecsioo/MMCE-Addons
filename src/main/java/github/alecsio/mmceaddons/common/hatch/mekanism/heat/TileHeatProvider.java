package github.alecsio.mmceaddons.common.hatch.mekanism.heat;

import com.google.common.util.concurrent.AtomicDouble;
import github.alecsio.mmceaddons.common.MMCEAConfig;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileEntityRestrictedTick;
import mekanism.api.IHeatTransfer;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.util.CapabilityUtils;
import mekanism.common.util.HeatUtils;
import mekanism.common.util.MekanismUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class TileHeatProvider extends TileEntityRestrictedTick implements MachineComponentTile, IAsyncRequirementHandler<RequirementHeat>, IHeatTransfer {

    protected final ReadWriteLock lock = new ReentrantReadWriteLock();
    protected final AtomicDouble temperature = new AtomicDouble(0);
    protected double heatToAbsorb = 0;

    public double getTemperature() {
        return temperature.get();
    }

    protected void dissipateHeat() {
        double localTemperature = temperature.get();
        if (localTemperature <= 0) {
            return;
        }
        float incr = (float)Math.sqrt(Math.abs(localTemperature)) * 0.2f;
        if (localTemperature > 0) {
            incr = -incr;
        }
        localTemperature = Math.max(0, localTemperature + incr);
        temperature.set(localTemperature);
    }

    @Override
    public double getTemp() {
        return getTemperature();
    }

    @Override
    public double getInverseConductionCoefficient() {
        return 5.0d;
    }

    @Override
    public double getInsulationCoefficient(EnumFacing enumFacing) {
        return 1000.0F;
    }

    @Override
    public double[] simulateHeat() {
        return HeatUtils.simulate(this);
    }

    @Override
    public void transferHeatTo(double v) {
        heatToAbsorb += v;
    }

    @Override
    public double applyTemperatureChange() {
        double newTemp = Math.min(temperature.get() + heatToAbsorb, MMCEAConfig.heatHatchesCapacity);
        heatToAbsorb = 0;
        temperature.set(newTemp);
        return newTemp;
    }

    @Override
    public boolean canConnectHeat(EnumFacing enumFacing) {
        return true;
    }

    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing side) {
        return capability == Capabilities.HEAT_TRANSFER_CAPABILITY || super.hasCapability(capability, side);
    }

    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing side) {
        return (T)(capability == Capabilities.HEAT_TRANSFER_CAPABILITY ? Capabilities.HEAT_TRANSFER_CAPABILITY.cast(this) : super.getCapability(capability, side));
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        temperature.set(compound.getDouble("temperature"));
        super.readCustomNBT(compound);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        compound.setDouble("temperature", temperature.get());
        super.writeCustomNBT(compound);
    }

    public static class Input extends TileHeatProvider {

        @Override
        public CraftCheck canHandleSync(RequirementHeat requirement) {
            lock.readLock().lock();
            try {
                return temperature.doubleValue() >= requirement.getTemperature() ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.heat.input");
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public CraftCheck canHandleAsync(RequirementHeat requirement) {
            return canHandleSync(requirement);
        }

        @Override
        public void handle(RequirementHeat requirement) {
            /* eats the temperature */
        }

        @Override
        public IHeatTransfer getAdjacent(EnumFacing enumFacing) {
            return null;
        }

        @Nullable
        @Override
        public MachineComponent<?> provideComponent() {
            return new MachineComponentHeatProvider(IOType.INPUT, this);
        }

        @Override
        public void doRestrictedTick() {
            if (!world.isRemote) {
                dissipateHeat();
                applyTemperatureChange();
                markNoUpdateSync();
            }
        }
    }

    public static class Output extends TileHeatProvider {

        @Override
        public CraftCheck canHandleSync(RequirementHeat requirement) {
            return temperature.doubleValue() + requirement.getTemperature() <= MMCEAConfig.heatHatchesCapacity ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.heat.output");
        }

        @Override
        public CraftCheck canHandleAsync(RequirementHeat requirement) {
            return canHandleSync(requirement);
        }

        @Override
        public void handle(RequirementHeat requirement) {
            lock.writeLock().lock();
            try {
                temperature.addAndGet(requirement.getTemperature());
                markNoUpdateSync();
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public IHeatTransfer getAdjacent(EnumFacing enumFacing) {
            TileEntity adj = MekanismUtils.getTileEntity(world, getPos().offset(enumFacing));
            return CapabilityUtils.getCapability(adj, Capabilities.HEAT_TRANSFER_CAPABILITY, enumFacing.getOpposite());
        }

        @Nullable
        @Override
        public MachineComponent<?> provideComponent() {
            return new MachineComponentHeatProvider(IOType.OUTPUT, this);
        }

        @Override
        public void doRestrictedTick() {
            if (!world.isRemote) {
                dissipateHeat();
                simulateHeat();
                applyTemperatureChange();
                markNoUpdateSync();
            }
        }
    }
}
