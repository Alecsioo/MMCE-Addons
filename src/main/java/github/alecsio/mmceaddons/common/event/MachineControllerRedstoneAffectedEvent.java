package github.alecsio.mmceaddons.common.event;

import github.kasuminova.mmce.common.event.machine.MachineEvent;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;

/**
 * This event is fired when the machine controller is affected by redstone. This allows processing components
 * to react to the machine being stopped, if anything needs to happen.
 */
public class MachineControllerRedstoneAffectedEvent extends MachineEvent {

    // The status of the machine (true if it is powered, false otherwise)
    public boolean isPowered;

    public MachineControllerRedstoneAffectedEvent(TileMultiblockMachineController controller, boolean isPowered) {
        super(controller);
        this.isPowered = isPowered;
    }
}
