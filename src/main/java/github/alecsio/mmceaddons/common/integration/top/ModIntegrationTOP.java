package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.Mods;
import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.apiimpl.TheOneProbeImp;

public class ModIntegrationTOP {

    public static void registerProviders() {
        TheOneProbeImp theOneProbeImp = TheOneProbe.theOneProbeImp;

        theOneProbeImp.registerProvider(new SnapshotMachineComponentInfoProvider());
        if (Mods.ABYSSALCRAFT.isPresent()) {
            theOneProbeImp.registerProvider(new PotentialEnergyInfoProvider());
        }
        if (Mods.ICE_AND_FIRE.isPresent()) {
            theOneProbeImp.registerProvider(new DragonBreathInfoProvider());
        }

        if (Mods.MEKANISM.isPresent()) {
            theOneProbeImp.registerProvider(new LaserInfoProvider());
            theOneProbeImp.registerProvider(new HeatInfoProvider());
        }
    }
}
