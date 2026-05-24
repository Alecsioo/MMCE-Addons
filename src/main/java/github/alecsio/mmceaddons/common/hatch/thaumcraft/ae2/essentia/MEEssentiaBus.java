package github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia;

import appeng.api.AEApi;
import appeng.api.networking.IGridNode;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.networking.ticking.IGridTickable;
import appeng.api.networking.ticking.TickRateModulation;
import appeng.api.networking.ticking.TickingRequest;
import appeng.api.storage.IMEInventory;
import appeng.api.util.AEPartLocation;
import github.alecsio.mmceaddons.common.hatch.handler.AdaptiveSnapshotRefreshScheduler;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import github.kasuminova.mmce.common.tile.base.MEMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import thaumicenergistics.api.storage.IAEEssentiaStack;
import thaumicenergistics.api.storage.IEssentiaStorageChannel;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static github.alecsio.mmceaddons.common.hatch.handler.AdaptiveSnapshotRefreshScheduler.MAX_INTERVAL_MS;
import static github.alecsio.mmceaddons.common.hatch.handler.AdaptiveSnapshotRefreshScheduler.MIN_INTERVAL_MS;

// Code was somehow adapted from a mix of whatever is being done in Thaumic Energistics and in other ME hatches in MMCE
public abstract class MEEssentiaBus extends MEMachineComponent implements IGridTickable, IAsyncRequirementHandler<RequirementEssentia> {

    protected final AdaptiveSnapshotRefreshScheduler refreshScheduler = new AdaptiveSnapshotRefreshScheduler(this::updateSnapshot);
    protected final ReadWriteLock lock = new ReentrantReadWriteLock();

    protected abstract void updateSnapshot();
    protected abstract CraftCheck checkSnapshot(RequirementEssentia requirement);

    @Override
    public CraftCheck canHandleSync(RequirementEssentia requirement) {
        lock.writeLock().lock();
        try {
            updateSnapshot();
            return checkSnapshot(requirement);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public CraftCheck canHandleAsync(RequirementEssentia requirement) {
        refreshScheduler.maybeScheduleRefresh();
        lock.readLock().lock();
        try {
            return checkSnapshot(requirement);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void handle(RequirementEssentia requirement) {
        updateSnapshot();
        refreshScheduler.maybeScheduleRefresh();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateSnapshot();
    }

    protected Optional<IMEInventory<IAEEssentiaStack>> getStorageInventory() {
        IGridNode gridNode = this.getGridNode(AEPartLocation.UP);
        if (gridNode == null) {
            return Optional.empty();
        }

        IStorageGrid storage = gridNode.getGrid().getCache(IStorageGrid.class);
        return Optional.of(storage.getInventory(getChannel()));
    }

    protected IEssentiaStorageChannel getChannel() {
        return AEApi.instance().storage().getStorageChannel(IEssentiaStorageChannel.class);
    }

    @Nonnull
    @Override
    public TickingRequest getTickingRequest(@Nonnull IGridNode iGridNode) {
        long now = System.currentTimeMillis();
        long idleMs = now - refreshScheduler.getLastSuccessTimestamp();
        boolean sleep = idleMs >= MAX_INTERVAL_MS;
        return new TickingRequest(Math.floorDiv(MIN_INTERVAL_MS, 50), Math.floorDiv(MAX_INTERVAL_MS, 50), sleep, true);
    }

    @Nonnull
    @Override
    public TickRateModulation tickingRequest(@Nonnull IGridNode iGridNode, int i) {
        updateSnapshot();

        long now = System.currentTimeMillis();
        long idleMs = now - refreshScheduler.getLastSuccessTimestamp();

        if (idleMs <= 0) {
            return TickRateModulation.URGENT;
        }

        if (idleMs >= MAX_INTERVAL_MS) {
            return TickRateModulation.SLEEP;
        }

        double ratio = Math.log1p(idleMs) / Math.log1p(MAX_INTERVAL_MS);

        if (ratio < 0.10) return TickRateModulation.URGENT;
        if (ratio < 0.25) return TickRateModulation.FASTER;
        if (ratio < 0.45) return TickRateModulation.SAME;
        if (ratio < 0.65) return TickRateModulation.SLOWER;
        if (ratio < 0.85) return TickRateModulation.IDLE;
        return TickRateModulation.SLEEP;
    }
}
