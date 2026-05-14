package github.alecsio.mmceaddons.common.hatch.wrapper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import thaumcraft.api.aura.AuraHelper;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static github.alecsio.mmceaddons.common.hatch.wrapper.BaseHelperWrapper.withLock;

// An attempt to make helper classes thread-safe by wrapping the calls to the original in locked blocks
public class AuraHelperWrapper {

    public static class Flux {

        private static final Lock lock = new ReentrantLock();

        public static double getFlux(World world, BlockPos pos) {
            return withLock(lock, () -> AuraHelper.getFlux(world, pos));
        }

        public static void addFlux(World world, BlockPos pos, float amount) {
            withLock(lock, () -> AuraHelper.polluteAura(world, pos, amount, false));
        }

        public static void drainFlux(World world, BlockPos pos, float amount) {
            withLock(lock, () -> {AuraHelper.drainFlux(world, pos, amount, false);});
        }
    }

    public static class Vis {

        private static final Lock lock = new ReentrantLock();

        public static double getVis(World world, BlockPos posInChunk) {
            return withLock(lock, () -> AuraHelper.getVis(world, posInChunk));
        }

        public static void addVis(World world, BlockPos posInChunk, float amountToAdd) {
            withLock(lock, () -> AuraHelper.addVis(world, posInChunk, amountToAdd));
        }

        public static void drainVis(World world, BlockPos posInChunk, float amountToDrain) {
            withLock(lock, () -> AuraHelper.drainVis(world, posInChunk, amountToDrain, false));
        }
    }
}
