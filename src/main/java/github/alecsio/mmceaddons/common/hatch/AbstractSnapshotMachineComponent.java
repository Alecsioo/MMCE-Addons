package github.alecsio.mmceaddons.common.hatch;

import github.alecsio.mmceaddons.common.hatch.handler.AdaptiveSnapshotRefreshScheduler;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractSnapshotMachineComponent<T> extends TileColorableMachineComponent implements IAsyncRequirementHandler<T> {

    protected final AdaptiveSnapshotRefreshScheduler refreshScheduler = new AdaptiveSnapshotRefreshScheduler(this::updateSnapshot);
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    protected abstract void updateSnapshot();
    protected abstract CraftCheck checkSnapshot(T requirement);


    @Override
    public CraftCheck canHandleSync(T requirement) {
        lock.writeLock().lock();
        try {
            updateSnapshot();
            return checkSnapshot(requirement);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public CraftCheck canHandleAsync(T requirement) {
        refreshScheduler.maybeScheduleRefresh();
        lock.readLock().lock();
        try {
            return checkSnapshot(requirement);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void handle(T requirement) {
        updateSnapshot();
        refreshScheduler.maybeScheduleRefresh();
    }

    public long getSecondsUntilNextRefresh() {
        return refreshScheduler.getSecondsUntilNextRefresh();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateSnapshot();
    }
}
