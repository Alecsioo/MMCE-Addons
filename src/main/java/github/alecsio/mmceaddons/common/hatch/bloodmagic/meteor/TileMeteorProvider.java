package github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor;

import WayofTime.bloodmagic.meteor.Meteor;
import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor.entity.EntityImprovedMeteor;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public abstract class TileMeteorProvider extends AbstractSnapshotMachineComponent<RequirementMeteor> implements MachineComponentTile {

    private static final int MAX_HEIGHT = 256;
    private static final int MAX_ALLOWED_BLOCKS_BETWEEN_TILE_AND_SKY = 1;

    public static class Output extends TileMeteorProvider {

        private EntityImprovedMeteor currentMeteor;
        private boolean isMeteorDead = true;
        private boolean canSeeSky = false;
        private BlockPos spawnPosition = null;

        @Nullable
        @Override
        public MachineComponentMeteorProvider provideComponent() {
            return new MachineComponentMeteorProvider(IOType.OUTPUT, this);
        }

        @Override
        protected CraftCheck checkSnapshot(RequirementMeteor meteor) {
            if (!isMeteorDead) {
                return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.meteor.alive");
            }
            if (!canSeeSky) {
                return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.meteor");
            }
            return CraftCheck.success();
        }

        @Override
        protected void updateSnapshot() {
            BlockPos pos = this.getPos();
            BlockPos spawnPos = pos;
            boolean localCanSeeSky = true;

            if (!world.canSeeSky(pos)) {
                int obstructedBlocks = 0;
                for (int y = pos.getY() + 1; y <= MAX_HEIGHT; y++) {
                    BlockPos toCheck = pos.up(y - pos.getY());
                    if (!world.isAirBlock(toCheck)) {
                        obstructedBlocks++;
                        spawnPos = toCheck.toImmutable();
                        if (obstructedBlocks > MAX_ALLOWED_BLOCKS_BETWEEN_TILE_AND_SKY) {
                            localCanSeeSky = false;
                            break;
                        }
                    }
                }
            }

            lock.writeLock().lock();
            try {
                isMeteorDead = currentMeteor == null || currentMeteor.isDead;
                this.canSeeSky = localCanSeeSky;
                this.spawnPosition = spawnPos;
            } finally {
                lock.writeLock().unlock();
            }
        }

        @Override
        public void handle(RequirementMeteor requirement) {
            Meteor meteor = requirement.getMeteor();
            this.currentMeteor = new EntityImprovedMeteor(world, pos.getX(), pos.getZ(), spawnPosition == null ? this.pos : spawnPosition, () -> {
                refreshScheduler.recordSuccess();
                updateSnapshot();
            });
            this.currentMeteor.setMeteorStack(meteor.getCatalystStack());
            world.spawnEntity(this.currentMeteor);
            markNoUpdateSync();
        }
    }
}
