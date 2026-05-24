package github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation;

import hellfirepvp.modularmachinery.common.machine.IOType;

public interface IRequirementRadiation {
    double getAmount();
    IOType getType();
    int getChunkRange();
}
