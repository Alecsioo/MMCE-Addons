package github.alecsio.mmceaddons.common.hatch.thaumcraft;

import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.storage.IMEInventory;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsBlocks;
import github.alecsio.mmceaddons.common.hatch.ae2.MEEssentiaBus;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import thaumcraft.api.aspects.Aspect;
import thaumicenergistics.api.EssentiaStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.integration.appeng.AEEssentiaStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class MEEssentiaOutputBus extends MEEssentiaBus {

    private static final String NBT_ASPECTS = "AspectAmounts";

    private Map<String, Long> aspectAmounts = new HashMap<>();

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        super.deserializeNBT(nbt);
        aspectAmounts = new HashMap<>();
        if (nbt.hasKey(NBT_ASPECTS)) {
            NBTTagCompound aspectsTag = nbt.getCompoundTag(NBT_ASPECTS);
            for (String key : aspectsTag.getKeySet()) {
                aspectAmounts.put(key, aspectsTag.getLong(key));
            }
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbt = super.serializeNBT();
        NBTTagCompound aspectsTag = new NBTTagCompound();
        aspectAmounts.forEach(aspectsTag::setLong);
        nbt.setTag(NBT_ASPECTS, aspectsTag);
        return nbt;
    }

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(ModularMachineryAddonsBlocks.blockMEEssentiaOutputBus);
    }

    @Nullable
    @Override
    public MachineComponent<IRequirementHandler<RequirementEssentia>> provideComponent() {
        return new MachineComponentEssentiaProvider(IOType.OUTPUT, this);
    }

    @Override
    protected void updateSnapshot() {
        try {
            this.proxy.getTick().wakeDevice(this.proxy.getNode());
        } catch (Exception e) {
            // :P
        }
    }

    @Nonnull
    @Override
    public TickRateModulation tickingRequest(@Nonnull IGridNode iGridNode, int i) {
        Optional<IMEInventory<IAEEssentiaStack>> optInventory = getStorageInventory();
        if (!optInventory.isPresent()) {
            return super.tickingRequest(iGridNode, i);
        }

        IMEInventory<IAEEssentiaStack> inventory = optInventory.get();
        boolean anyInserted = false;
        boolean anyRemaining = false;

        lock.writeLock().lock();
        try {
            Iterator<Map.Entry<String, Long>> iterator = aspectAmounts.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();

                Aspect aspect = Aspect.getAspect(entry.getKey());
                if (aspect == null) {
                    iterator.remove();
                    continue;
                }

                IAEEssentiaStack toInsert = AEEssentiaStack.fromEssentiaStack(new EssentiaStack(entry.getKey(), entry.getValue().intValue()));
                IAEEssentiaStack notInserted = inventory.injectItems(toInsert, Actionable.MODULATE, this.source);

                long leftover = notInserted != null ? notInserted.getStackSize() : 0L;

                if (leftover <= 0) {
                    iterator.remove();
                    anyInserted = true;
                } else {
                    entry.setValue(leftover);
                    anyRemaining = true;
                    if (leftover < toInsert.getStackSize()) {
                        anyInserted = true;
                    }
                }
            }
            if (anyInserted || anyRemaining) {
                refreshScheduler.recordSuccess();
            }
            return super.tickingRequest(iGridNode, i);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected CraftCheck checkSnapshot(RequirementEssentia requirement) {
        EssentiaStack essentiaStack = requirement.getEssentiaStack();
        Long storedAmount = aspectAmounts.get(essentiaStack.getAspect().getTag());
        return storedAmount == null ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.essentia.output");
    }

    @Override
    public void handle(RequirementEssentia requirement) {
        Optional<IMEInventory<IAEEssentiaStack>> optInventory = getStorageInventory();
        if (!optInventory.isPresent()) {
            return;
        }

        IAEEssentiaStack toInsert = AEEssentiaStack.fromEssentiaStack(requirement.getEssentiaStack());
        IAEEssentiaStack notInserted = optInventory.get().injectItems(toInsert, Actionable.MODULATE, this.source);

        if (notInserted != null && notInserted.getStackSize() > 0) {
            String aspectName = notInserted.getAspect().getTag();
            long leftover = notInserted.getStackSize();

            lock.writeLock().lock();
            try {
                aspectAmounts.merge(aspectName, leftover, Long::sum);
            } finally {
                lock.writeLock().unlock();
            }
        }

        super.handle(requirement);
    }
}
