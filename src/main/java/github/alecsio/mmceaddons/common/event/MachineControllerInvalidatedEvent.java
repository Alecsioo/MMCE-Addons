package github.alecsio.mmceaddons.common.event;

import github.kasuminova.mmce.common.event.machine.MachineEvent;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;

public class MachineControllerInvalidatedEvent extends MachineEvent {

    public MachineControllerInvalidatedEvent(TileMultiblockMachineController controller) {
        super(controller);
    }
}
