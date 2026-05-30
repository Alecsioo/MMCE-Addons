package github.alecsio.mmceaddons.common.hatch.handler;

import github.alecsio.mmceaddons.common.MMCEAConfig;
import github.kasuminova.mmce.common.util.concurrent.Action;
import hellfirepvp.modularmachinery.ModularMachinery;

public class AdaptiveSnapshotRefreshScheduler {

    private volatile long lastSuccessTimestamp = 0L;
    private volatile long lastScheduledAt = 0L;
    private final Action refreshAction;

    public AdaptiveSnapshotRefreshScheduler(Action refreshAction) {
        this.refreshAction = refreshAction;
    }

    public void recordSuccess() {
        lastSuccessTimestamp = System.currentTimeMillis();
    }

    public void maybeScheduleRefresh() {
        long now = System.currentTimeMillis();
        long interval = computeInterval(now - lastSuccessTimestamp);
        if (now - lastScheduledAt >= interval) {
            lastScheduledAt = now;
            ModularMachinery.EXECUTE_MANAGER.addSyncTask(refreshAction);
        }
    }

    /**
     * Returns the absolute wall-clock timestamp, in milliseconds since epoch,
     * at which the next refresh becomes eligible to be scheduled.
     *
     * <p>This is derived from the last time a refresh was scheduled plus the
     * current adaptive interval computed from the time since the last success.
     *
     * @return the next eligible refresh timestamp in milliseconds
     */
    public long getNextRefreshTimestamp() {
        long now = System.currentTimeMillis();
        long interval = computeInterval(now - lastSuccessTimestamp);
        return lastScheduledAt + interval;
    }

    /**
     * Returns how many milliseconds remain until the next refresh is eligible
     * to be scheduled.
     *
     * <p>If the refresh is already due, this returns 0.
     *
     * @return remaining delay in milliseconds until the next refresh
     */
    public long getSecondsUntilNextRefresh() {
        return Math.max(0L, getNextRefreshTimestamp() - System.currentTimeMillis()) / 1000;
    }

    public long getLastSuccessTimestamp() {
        return lastSuccessTimestamp;
    }

    /**
     * Maps {@code idleMs} to a polling interval.
     *
     * <p>Uses a logarithmic curve so that:
     * <ul>
     *   <li>0 idle time → {@code MIN_INTERVAL_MS}</li>
     *   <li>{@code MAX_INTERVAL_MS} or more idle → {@code MAX_INTERVAL_MS}</li>
     * </ul>
     *
     * @param idleMs milliseconds since the last known success
     * @return the interval to wait before the next refresh, in milliseconds
     */
    private long computeInterval(long idleMs) {
        if (idleMs <= 0) return MMCEAConfig.minIntervalMs;
        if (idleMs >= MMCEAConfig.maxIntervalMs) return MMCEAConfig.maxIntervalMs;
        double ratio = Math.log1p(idleMs) / Math.log1p(MMCEAConfig.maxIntervalMs);
        return MMCEAConfig.minIntervalMs + (long) (ratio * (MMCEAConfig.maxIntervalMs - MMCEAConfig.minIntervalMs));
    }

}
