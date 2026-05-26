package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.hatch.mekanism.laser.TileLaserProvider;
import mcjty.theoneprobe.api.IProbeInfo;

public class LaserInfoProvider extends BaseInfoProvider<TileLaserProvider> {

    public LaserInfoProvider() {
        super(TileLaserProvider.class);
    }

    @Override
    protected String getName() {
        return "laser_provider";
    }

    @Override
    protected void addProbeInfo(IProbeInfo iProbeInfo, TileLaserProvider hatch) {
        iProbeInfo.text(wrapInLoc("top.modularmachineryaddons.laser") + " " + hatch.getStoredEnergy());
    }
}
