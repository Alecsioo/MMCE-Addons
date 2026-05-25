package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.hatch.mekanism.heat.TileHeatProvider;
import mcjty.theoneprobe.api.IProbeInfo;
import mekanism.common.util.MekanismUtils;
import mekanism.common.util.UnitDisplayUtils;

public class HeatInfoProvider extends BaseInfoProvider<TileHeatProvider> {

    public HeatInfoProvider() {
        super(TileHeatProvider.class);
    }

    @Override
    protected String getName() {
        return "heat_provider";
    }

    @Override
    protected void addProbeInfo(IProbeInfo iProbeInfo, TileHeatProvider hatch) {
        iProbeInfo.text(wrapInLoc("top.modularmachineryaddons.heat") + " " + MekanismUtils.getTemperatureDisplay(hatch.getTemperature(), UnitDisplayUtils.TemperatureUnit.CELSIUS));
    }
}
