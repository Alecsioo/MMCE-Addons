package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.hatch.abyssalcraft.TilePotentialEnergyProvider;
import mcjty.theoneprobe.api.IProbeInfo;
import net.minecraft.client.resources.I18n;

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
        iProbeInfo.text(I18n.format("top.modularmachineryaddons.potential_energy", hatch.getContainedEnergy()));
    }
}
