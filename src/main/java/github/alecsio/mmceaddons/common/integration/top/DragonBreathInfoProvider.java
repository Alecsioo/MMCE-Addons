package github.alecsio.mmceaddons.common.integration.top;

import github.alecsio.mmceaddons.common.hatch.iceandfire.TileDragonBreathProvider;
import mcjty.theoneprobe.api.IProbeInfo;

public class DragonBreathInfoProvider extends BaseInfoProvider<TileDragonBreathProvider> {

    public DragonBreathInfoProvider() {
        super(TileDragonBreathProvider.class);
    }

    @Override
    protected String getName() {
        return "dragon_breath";
    }

    @Override
    protected void addProbeInfo(IProbeInfo iProbeInfo, TileDragonBreathProvider hatch) {
        iProbeInfo.text(wrapInLoc((hatch.isTypeLocked() ? "top.modularmachineryaddons.dragon_breath.locked.true" : "top.modularmachineryaddons.dragon_breath.locked.false")));
        String dragonTypeKey = hatch.getType() == null ? "N/A" : String.format("dragon.type.%s", hatch.getType().name().toLowerCase());
        iProbeInfo.text(wrapInLoc("top.modularmachineryaddons.dragon_breath.type")  + " " + wrapInLoc(dragonTypeKey));
        iProbeInfo.text(wrapInLoc("top.modularmachineryaddons.dragon_breath.quantity")  + " " + hatch.getCharges());
    }
}
