package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import mcjty.theoneprobe.api.IProbeInfo;

@SuppressWarnings("raw")
public class SnapshotMachineComponentInfoProvider extends BaseInfoProvider<AbstractSnapshotMachineComponent> {

    public SnapshotMachineComponentInfoProvider() {
        super(AbstractSnapshotMachineComponent.class);
    }

    @Override
    protected String getName() {
        return "snapshot_machine_info_provider";
    }

    @Override
    protected void addProbeInfo(IProbeInfo iProbeInfo, AbstractSnapshotMachineComponent hatch) {
        iProbeInfo.text(wrapInLoc("top.modularmachineryaddons.next_refresh") + " " + hatch.getSecondsUntilNextRefresh());
    }

}
