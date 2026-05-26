package github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia;

import appeng.api.config.Actionable;
import appeng.api.storage.IMEInventory;
import appeng.api.storage.data.IItemList;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Essentia;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsBlocks;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import net.minecraft.item.ItemStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.integration.appeng.AEEssentiaStack;
import thaumicenergistics.integration.appeng.EssentiaList;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MEEssentiaInputBus extends MEEssentiaBus {

    private Map<String, Long> aspectAmounts = new HashMap<>();

    @Override
    public ItemStack getVisualItemStack() {
        return new ItemStack(ModularMachineryAddonsBlocks.blockMEEssentiaInputBus);
    }

    @Nullable
    @Override
    public MachineComponent<IRequirementHandler<RequirementEssentia>> provideComponent() {
        return new MachineComponentEssentiaProvider(IOType.INPUT, this);
    }

    @Override
    protected void updateSnapshot() {
        Map<String, Long> localAspectAmounts = new HashMap<>();

        Optional<IMEInventory<IAEEssentiaStack>> optInventory = getStorageInventory();
        if (!optInventory.isPresent()) {
            return;
        }

        IItemList<IAEEssentiaStack> essentiaList = optInventory.get().getAvailableItems(new EssentiaList());

        for (IAEEssentiaStack ess : essentiaList) {
            localAspectAmounts.put(ess.getAspect().getTag(), ess.getStackSize());
        }

        lock.writeLock().lock();
        try {
            aspectAmounts = localAspectAmounts;
            markNoUpdateSync();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    protected CraftCheck checkSnapshot(RequirementEssentia requirement) {
        Essentia essentia = requirement.getEssentiaStack();
        Long amount = aspectAmounts.getOrDefault(essentia.getAspectTag(), 0L);
        return amount >= essentia.getAmount() ? CraftCheck.success() : CraftCheck.failure("error.modularmachineryaddons.requirement.missing.essentia.input");
    }


    @Override
    public void handle(RequirementEssentia requirement) {
        Optional<IMEInventory<IAEEssentiaStack>> optInventory = getStorageInventory();
        if (!optInventory.isPresent()) {
            return;
        }

        optInventory.get().extractItems(AEEssentiaStack.fromEssentiaStack(requirement.getEssentiaStack()), Actionable.MODULATE, this.source);

        super.handle(requirement);
    }
}
