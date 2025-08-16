package github.alecsio.mmceaddons.common.tile.bloodmagic;

import WayofTime.bloodmagic.meteor.Meteor;
import github.alecsio.mmceaddons.common.crafting.requirement.bloodmagic.RequirementMeteor;
import github.alecsio.mmceaddons.common.entity.EntityImprovedMeteor;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentMeteorProvider;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class TileMeteorProvider extends TileColorableMachineComponent implements MachineComponentTile, IRequirementHandler<RequirementMeteor> {

    private static final int MAX_HEIGHT = 256;
    private static final int MAX_ALLOWED_BLOCKS_BETWEEN_TILE_AND_SKY = 1;

    public static class Output extends TileMeteorProvider {

        private EntityImprovedMeteor currentMeteor;
        private BlockPos spawnPos;
        private final ReadWriteLock lock = new ReentrantReadWriteLock();

        @Nullable
        @Override
        public MachineComponentMeteorProvider provideComponent() {
            return new MachineComponentMeteorProvider(IOType.OUTPUT, this);
        }

        @Override
        public CraftCheck canHandle(RequirementMeteor requirement) {
            lock.readLock().lock();
            try {
                if (currentMeteor != null && !currentMeteor.isDead) {
                    return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.meteor.alive");
                }


                BlockPos pos = this.getPos();
                BlockPos spawnPos = this.getPos();
                if (!world.canSeeSky(pos)) {
                    int obstructedBlocks = 0;
                    for (int y = pos.getY() + 1; y <= MAX_HEIGHT; y++) {
                        BlockPos toCheck = pos.up(y - pos.getY());
                        if (!world.isAirBlock(toCheck)) {
                            obstructedBlocks++;
                            spawnPos = toCheck.toImmutable();
                            if (obstructedBlocks > MAX_ALLOWED_BLOCKS_BETWEEN_TILE_AND_SKY) {
                                return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.meteor");
                            }
                        }
                    }
                }

                this.spawnPos = spawnPos;
                return CraftCheck.success();
            } finally {
                lock.readLock().unlock();
            }
        }

        @Override
        public void handle(RequirementMeteor requirement) {
            lock.writeLock().lock();
            try {
                Meteor meteor = requirement.getMeteor();
                this.currentMeteor = new EntityImprovedMeteor(world, pos.getX(), pos.getZ(), spawnPos == null ? this.pos : spawnPos);
                this.currentMeteor.setMeteorStack(meteor.getCatalystStack());
                world.spawnEntity(this.currentMeteor);
                markNoUpdateSync();
            } finally {
                lock.writeLock().unlock();
            }
        }
    }
}
