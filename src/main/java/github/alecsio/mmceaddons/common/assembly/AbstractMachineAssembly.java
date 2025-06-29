package github.alecsio.mmceaddons.common.assembly;

import ink.ikx.mmce.common.utils.StructureIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractMachineAssembly implements IMachineAssembly {

    protected final World world;
    protected final BlockPos controllerPos;
    protected final EntityPlayer player;
    protected StructureIngredient ingredient;

    protected boolean completed = false;

    public AbstractMachineAssembly(World world, BlockPos controllerPos, EntityPlayer player, StructureIngredient ingredient) {
        this.world = world;
        this.controllerPos = controllerPos;
        this.player = player;
        this.ingredient = ingredient;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }
}
