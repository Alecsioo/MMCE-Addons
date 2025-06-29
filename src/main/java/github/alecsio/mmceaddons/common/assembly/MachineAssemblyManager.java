package github.alecsio.mmceaddons.common.assembly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MachineAssemblyManager {

    private static final HashMap<BlockPos, IMachineAssembly> MACHINE_ASSEMBLY_MAP = new HashMap<>();

    public static void addMachineAssembly(IMachineAssembly machineAssembly) {
        MACHINE_ASSEMBLY_MAP.put(machineAssembly.getControllerPos(), machineAssembly);
    }

    public static boolean machineExists(BlockPos ctrlPos) {
        return MACHINE_ASSEMBLY_MAP.containsKey(ctrlPos);
    }

    public static Collection<IMachineAssembly> getMachineAssemblyListFromPlayer(EntityPlayer player) {
        return MACHINE_ASSEMBLY_MAP.values().stream()
                .filter(assembly -> player.getGameProfile().getId().equals(
                        assembly.getPlayer().getGameProfile().getId()))
                .collect(Collectors.toList());
    }

    public static void removeMachineAssembly(BlockPos controllerPos) {
        MACHINE_ASSEMBLY_MAP.remove(controllerPos);
    }

    public static void removeMachineAssembly(EntityPlayer player) {
        List<BlockPos> willBeRemoved = new ArrayList<>();
        for (final IMachineAssembly assembly : MACHINE_ASSEMBLY_MAP.values()) {
            if (assembly.getPlayer().equals(player)) {
                willBeRemoved.add(assembly.getControllerPos());
            }
        }
        willBeRemoved.forEach(ink.ikx.mmce.common.assembly.MachineAssemblyManager::removeMachineAssembly);
    }
}
