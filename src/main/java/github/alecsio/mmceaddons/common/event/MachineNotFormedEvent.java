package github.alecsio.mmceaddons.common.event;

import github.kasuminova.mmce.common.event.machine.MachineEvent;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;

public class MachineNotFormedEvent extends MachineEvent {

    public MachineNotFormedEvent(TileMultiblockMachineController controller) {
        super(controller);
    }
}
