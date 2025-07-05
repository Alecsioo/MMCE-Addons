package github.alecsio.mmceaddons.common.mixin;

import github.alecsio.mmceaddons.common.event.MachineControllerInvalidatedEvent;
import github.alecsio.mmceaddons.common.event.MachineNotFormedEvent;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftingStatus;
import hellfirepvp.modularmachinery.common.tiles.TileMachineController;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileMachineController.class)
public abstract class MachineNotFormedEventMixin extends TileMultiblockMachineController {

    @Inject(method = "setControllerStatus(Lhellfirepvp/modularmachinery/common/crafting/helper/CraftingStatus;)V",
    at = @At(value = "HEAD"),
    remap = false)
    private void onMachineFirstNotFormed(CraftingStatus status, CallbackInfo ci) {
        TileMultiblockMachineController.Type type = status.getStatus();
        if (!(type.equals(CraftingStatus.MISSING_STRUCTURE.getStatus()) || type.equals(CraftingStatus.CHUNK_UNLOADED.getStatus()))) {
            return;
        }
        MachineNotFormedEvent event = new MachineNotFormedEvent(this);
        event.postEvent();
    }

    @Inject(method = "invalidate",
    at = @At(value = "HEAD"),
    remap = false)
    private void onMachineControllerInvalidated(CallbackInfo ci) {
        MachineControllerInvalidatedEvent machineControllerInvalidatedEvent = new MachineControllerInvalidatedEvent(this);
        machineControllerInvalidatedEvent.postEvent();
    }
}
