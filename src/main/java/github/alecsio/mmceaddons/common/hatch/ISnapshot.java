package github.alecsio.mmceaddons.common.hatch;

public interface ISnapshot<T extends IMultiChunkRequirement> {
    ISnapshot<T> getSnapshotForRange(int range);
    boolean canHandleInput(T requirement);
    boolean canHandleOutput(T requirement);
}
