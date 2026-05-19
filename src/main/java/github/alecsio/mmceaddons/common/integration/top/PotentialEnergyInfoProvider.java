package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.hatch.abyssalcraft.TilePotentialEnergyProvider;
import mcjty.theoneprobe.api.IProbeInfo;

public class PotentialEnergyInfoProvider extends BaseInfoProvider<TilePotentialEnergyProvider> {

    public PotentialEnergyInfoProvider() {
        super(TilePotentialEnergyProvider.class);
    }

    @Override
    protected String getName() {
        return "potential_energy";
    }

    @Override
    protected void addProbeInfo(IProbeInfo iProbeInfo, TilePotentialEnergyProvider hatch) {
        iProbeInfo.text(wrapInLoc("top.modularmachineryaddons.potential_energy") + " " + hatch.getContainedEnergy());
    }
}
