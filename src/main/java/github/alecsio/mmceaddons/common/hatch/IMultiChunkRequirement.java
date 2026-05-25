package github.alecsio.mmceaddons.common.hatch;

import hellfirepvp.modularmachinery.common.machine.IOType;

// I want multiple inheritance
public interface IMultiChunkRequirement {

    IOType getIOType();

    int getChunkRange();

    double getAmount();

    double getMinPerChunk();

    double getMaxPerChunk();
}
