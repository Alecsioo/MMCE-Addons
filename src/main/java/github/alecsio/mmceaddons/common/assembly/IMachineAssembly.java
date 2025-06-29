package github.alecsio.mmceaddons.common.assembly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

public interface IMachineAssembly {

    BlockPos getControllerPos();
    EntityPlayer getPlayer();
    void assembleTick();
    boolean isCompleted();
}
