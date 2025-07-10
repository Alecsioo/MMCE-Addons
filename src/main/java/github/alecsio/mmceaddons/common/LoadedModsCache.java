package github.alecsio.mmceaddons.common;

import net.minecraftforge.fml.common.Loader;

public class LoadedModsCache {

    public static final String AE2 = "appliedenergistics2";
    public static final String PROJECTE = "projecte";
    public static final String AS = "astralsorcery";

    public static boolean aeLoaded = false;
    public static boolean projecteLoaded = false;
    public static boolean asLoaded = false;
    public static boolean initialized = false;

    public static void initialize() {
        if (initialized) return;

        aeLoaded = Loader.isModLoaded(AE2);
        projecteLoaded = Loader.isModLoaded(PROJECTE);
        asLoaded = Loader.isModLoaded(AS);
    }
}
