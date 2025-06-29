package github.alecsio.mmceaddons.common.assembly;

import com.github.bsideup.jabel.Desugar;
import github.alecsio.mmceaddons.common.LoadedModsCache;

import java.util.*;

@Desugar
public enum AssemblyModes {

    PLAYER_INVENTORY(Collections.unmodifiableSet(EnumSet.of(AssemblySupportedMods.VANILLA))),
    PLAYER_INVENTORY_EMC(Collections.unmodifiableSet(EnumSet.of(AssemblySupportedMods.VANILLA, AssemblySupportedMods.PROJECTE))),
    PLAYER_INVENTORY_ME(Collections.unmodifiableSet(EnumSet.of(AssemblySupportedMods.VANILLA, AssemblySupportedMods.APPLIEDENERGISTICS2))),
    ALL(Collections.unmodifiableSet(EnumSet.of(AssemblySupportedMods.VANILLA, AssemblySupportedMods.PROJECTE, AssemblySupportedMods.APPLIEDENERGISTICS2))),;


    private final Set<AssemblySupportedMods> requiredMods;
    private static final List<AssemblyModes> supportedModes = new ArrayList<>();

    AssemblyModes(Set<AssemblySupportedMods> requiredMods) {
        this.requiredMods = requiredMods;
    }

    public boolean supports(AssemblySupportedMods mod) {
        return requiredMods.contains(mod);
    }

    public static List<AssemblyModes> getSupportedModes() {
        if (!supportedModes.isEmpty()) {
            return supportedModes;
        }

        supportedModes.add(PLAYER_INVENTORY);

        if (LoadedModsCache.projecteLoaded) {
            supportedModes.add(AssemblyModes.PLAYER_INVENTORY_EMC);
        }

        if (LoadedModsCache.aeLoaded) {
            supportedModes.add(AssemblyModes.PLAYER_INVENTORY_ME);
        }

        if (LoadedModsCache.aeLoaded && LoadedModsCache.projecteLoaded) {
            supportedModes.add(AssemblyModes.ALL);
        }

        return supportedModes;
    }
}
