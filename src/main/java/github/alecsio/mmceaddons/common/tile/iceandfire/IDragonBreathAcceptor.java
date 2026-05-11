package github.alecsio.mmceaddons.common.tile.iceandfire;

import github.alecsio.mmceaddons.common.crafting.requirement.DragonType;

public interface IDragonBreathAcceptor {

    void lureDragons();
    void onHitWithFlame(DragonType dragonType);

}
