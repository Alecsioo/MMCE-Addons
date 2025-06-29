package github.alecsio.mmceaddons.common.assembly.handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.MachineAssemblyManager;
import github.alecsio.mmceaddons.common.config.MMCEAConfig;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import ink.ikx.mmce.core.AssemblyConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class MachineAssemblyEventHandler {

    public static final Cache<EntityPlayer, Boolean> ASSEMBLY_ACCESS_TOKEN = CacheBuilder.newBuilder()
            .expireAfterWrite(MMCEAConfig.cooldown, TimeUnit.SECONDS)
            .build();

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        if (event.phase == TickEvent.Phase.START || world.isRemote || world.getTotalWorldTime() % AssemblyConfig.tickBlock != 0) {
            return;
        }

        Collection<IMachineAssembly> machineAssemblies = MachineAssemblyManager.getMachineAssemblyListFromPlayer(player);
        if (machineAssemblies == null || machineAssemblies.isEmpty()) {
            return;
        }

        ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> {
            for (final IMachineAssembly assembly : machineAssemblies) {
                if (!(world.getTileEntity(assembly.getControllerPos()) instanceof TileMultiblockMachineController)) {
                    MachineAssemblyManager.removeMachineAssembly(assembly.getControllerPos());
                    player.sendMessage(new TextComponentTranslation("message.assembly.tip.cancelled"));
                    return;
                }
                assembly.assembleTick();
                if (assembly.isCompleted()) {
                    MachineAssemblyManager.removeMachineAssembly(assembly.getControllerPos());
                    player.sendMessage(new TextComponentTranslation("message.assembly.tip.success"));
                }
            }
        });
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        MachineAssemblyManager.removeMachineAssembly(event.player);
    }

}
