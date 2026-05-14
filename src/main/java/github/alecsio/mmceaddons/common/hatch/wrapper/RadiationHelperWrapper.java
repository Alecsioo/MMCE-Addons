package github.alecsio.mmceaddons.common.hatch.wrapper;

import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import net.minecraft.world.chunk.Chunk;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static github.alecsio.mmceaddons.common.hatch.wrapper.BaseHelperWrapper.withLock;

// An attempt to make helper classes thread-safe by wrapping the calls to the original in locked blocks
public class RadiationHelperWrapper {

    private static final Lock lock = new ReentrantLock();

    public static double getRadiationAmount(Chunk chunk) {
        return withLock(lock, () -> getSourceFrom(chunk).getRadiationLevel());
    }

    public static void scrubRadiation(Chunk chunk) {
        withLock(lock, () -> {
            var source = getSourceFrom(chunk);
            source.setRadiationLevel(0);
            source.setRadiationBuffer(0);
        });
    }

    public static void increaseRadiationBuffer(Chunk chunk, double amountToAdd) {
        withLock(lock, () -> getSourceFrom(chunk).setRadiationBuffer(getSourceFrom(chunk).getRadiationLevel() + amountToAdd));
    }

    public static void decreaseRadiationLevel(Chunk chunk, double amountToDecrease) {
        withLock(lock, () -> getSourceFrom(chunk).setRadiationLevel(getSourceFrom(chunk).getRadiationLevel() - amountToDecrease));
    }

    private static IRadiationSource getSourceFrom(Chunk chunk) {
        return RadiationHelper.getRadiationSource(chunk);
    }




}
