package github.alecsio.mmceaddons.common.tile.ae2;

import appeng.api.AEApi;
import appeng.api.config.Actionable;
import appeng.api.networking.IGridNode;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.util.AEPartLocation;
import github.alecsio.mmceaddons.common.crafting.requirement.thaumicenergistics.RequirementEssentia;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.kasuminova.mmce.common.tile.base.MEMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import thaumicenergistics.api.EssentiaStack;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

import javax.annotation.Nonnull;
import java.util.Objects;

// Code was somehow adapted from a mix of whatever is being done in Thaumic Enenrgistics and in other ME hatches in MMCE
public abstract class MEEssentiaBus extends MEMachineComponent implements IGridTickable, IRequirementHandler<RequirementEssentia> {

    protected IMEInventory<IAEEssentiaStack> getStorageInventory() {
        IStorageGrid storage = Objects.requireNonNull(this.getGridNode(AEPartLocation.UP)).getGrid().getCache(IStorageGrid.class);
        return storage.getInventory(getChannel());
    }

    protected IEssentiaStorageChannel getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }

    protected abstract boolean canPerformOperation(Actionable actionable, EssentiaStack essentia);

    @Nonnull
    @Override
    public TickingRequest getTickingRequest(@Nonnull IGridNode iGridNode) {
        return new TickingRequest(5, 40, true, true) {}; // Magic numbers
    }

    @Nonnull
    @Override
    public TickRateModulation tickingRequest(@Nonnull IGridNode iGridNode, int i) {
        return TickRateModulation.SLEEP;
    }

    @Override
    public CraftCheck canHandle(RequirementEssentia essentia) {
        boolean canPerformOperation = canPerformOperation(Actionable.SIMULATE, essentia.getEssentiaStack());
        if (canPerformOperation) {return CraftCheck.success();}

        if (essentia.getActionType().equals(IOType.INPUT)) {

        } else {

        }

        return canPerformOperation(Actionable.SIMULATE, essentia.getEssentiaStack()) ? CraftCheck.success() : CraftCheck.failure(getKeyForRequirement(essentia));
    }

    @Override
    public void handle(RequirementEssentia essentia) {
        canPerformOperation(Actionable.MODULATE, essentia.getEssentiaStack());
    }

    private String getKeyForRequirement(RequirementEssentia requirement) {
        return requirement.getActionType().equals(IOType.INPUT) ? "error.modularmachineryaddons.requirement.missing.essentia.input" : "error.modularmachineryaddons.requirement.missing.essentia.output";
    }
}
