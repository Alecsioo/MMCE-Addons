package github.alecsio.mmceaddons.common.hatch.bloodmagic.entity;

import WayofTime.bloodmagic.entity.projectile.EntityMeteor;
import github.kasuminova.mmce.common.util.concurrent.Action;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityImprovedMeteor extends EntityMeteor {

    private static final int SPAWN_Y = 260;
    private static final double VEL_X = 0d, VEL_Y = -0.1d, VEL_Z = VEL_X;
    private static final double RADIUS_MODIFIER = 1d;
    // Do not break any blocks
    private static final double EXPLOSION_MODIFIER = 0d;
    // In vanilla BM this is calculated with amount of corrosive will / 200
    private static final double FILLER_CHANCE = 0d;

    private final BlockPos spawnPos;
    private Action onHitAction;

    // Used by ... something. For some reason this constructor is being retrieved with reflection and if it's not there
    // it will crash during init
    public EntityImprovedMeteor(World world) {
        super(world);
        spawnPos = null;
    }

    public EntityImprovedMeteor(World world, double x, double z, BlockPos spawnPos) {
        super(world, x, SPAWN_Y, z, VEL_X, VEL_Y, VEL_Z, RADIUS_MODIFIER, EXPLOSION_MODIFIER, FILLER_CHANCE);
        this.spawnPos = spawnPos;
    }

    public EntityImprovedMeteor(World world, double x, double z, BlockPos spawnPos, Action onHit) {
        this(world, x, z, spawnPos);
        this.onHitAction = onHit;
    }

    /**
     * Custom imp that disregards impact with entities and spawns the meteor at a predefined block position instead of
     * the hit pos
     */
    @Override
    protected void onImpact(RayTraceResult mop) {
        if (mop.typeOfHit == RayTraceResult.Type.BLOCK) {
            generateMeteor(spawnPos);
            this.setDead();
            if (onHitAction != null) {
                onHitAction.doAction();
            }
        }
    }

    @Override
    public void onKillCommand() {
        super.onKillCommand();
        if (onHitAction != null) {
            onHitAction.doAction();
        }
    }

}
