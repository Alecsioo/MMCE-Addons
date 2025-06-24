package github.alecsio.mmceaddons.common.mixin;

import github.alecsio.mmceaddons.common.event.MachineControllerRedstoneAffectedEvent;
import hellfirepvp.modularmachinery.common.tiles.TileMachineController;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import scala.tools.asm.Opcodes;

@Pseudo
@Mixin(TileMachineController.class)
public abstract class MachineControllerRedstoneAffectedEventMixin extends TileMultiblockMachineController {

    @Inject(
            method = "doControllerTick()V",
            at = @At(value = "FIELD", target = "Lhellfirepvp/modularmachinery/common/tiles/TileMachineController;redstoneEffected:Z",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER), remap = false)
    private void onControllerPowered(CallbackInfo ci) {
        new MachineControllerRedstoneAffectedEvent(this, true).postEvent();
    }

    @Inject(
            method = "canCheckStructure()Z",
            at = @At(value = "FIELD", target = "Lhellfirepvp/modularmachinery/common/tiles/TileMachineController;redstoneEffected:Z",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER), remap = false)
    private void onControllerUnpowered(CallbackInfoReturnable<Boolean> cir) {
        new MachineControllerRedstoneAffectedEvent(this, false).postEvent();
    }
}
