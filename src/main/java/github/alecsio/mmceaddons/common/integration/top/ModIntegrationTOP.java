package github.alecsio.mmceaddons.common.integration.top;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;

public class ModIntegrationTOP {

    public static void registerProviders() {
        TheOneProbeImp theOneProbeImp = TheOneProbe.theOneProbeImp;

        theOneProbeImp.registerProvider(new PotentialEnergyInfoProvider());
        theOneProbeImp.registerProvider(new DragonBreathInfoProvider());
    }
}
