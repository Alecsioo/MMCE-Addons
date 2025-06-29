package github.alecsio.mmceaddons.common.event;

import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import net.minecraftforge.fml.common.eventhandler.Event;

import java.util.List;

/**
 * This event is fired when the machine controller is first affected by redstone. This allows processing components
 * to react to the machine being stopped, if anything needs to happen.
 */
public class MachineControllerRedstoneAffectedEvent extends Event {

    // The currently found components for the affected controller
    public List<ProcessingComponent<?>> components;
    public boolean isPowered;

    public MachineControllerRedstoneAffectedEvent(List<ProcessingComponent<?>> components, boolean isPowered) {
        this.components = components;
        this.isPowered = isPowered;
    }
}
