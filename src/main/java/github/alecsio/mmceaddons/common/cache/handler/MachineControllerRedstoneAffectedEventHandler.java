package github.alecsio.mmceaddons.common.cache.handler;

import github.alecsio.mmceaddons.common.event.MachineControllerRedstoneAffectedEvent;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentScrubberProvider;
import github.alecsio.mmceaddons.common.tile.nuclearcraft.TileScrubberProvider;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MachineControllerRedstoneAffectedEventHandler {

    @SubscribeEvent
    public void onMachineControllerRedstoneAffectedEvent(MachineControllerRedstoneAffectedEvent event) {
        for (ProcessingComponent<?> component : event.components) {
            if (component.getComponent() instanceof MachineComponentScrubberProvider scrubberProvider) {
                IRequirementHandler<?> handler = scrubberProvider.getContainerProvider();
                if (handler instanceof TileScrubberProvider scrubber) {
                    if (event.isPowered) {
                        scrubber.unmarkAllChunksAsScrubbed();
                    } else {
                        scrubber.markAllChunksAsScrubbed();
                    }
                }
            }
        }
    }
}
