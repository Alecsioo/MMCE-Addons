package github.alecsio.mmceaddons.common.base;

import net.minecraftforge.fml.common.Loader;

public enum Mods {
    NUCLEARCRAFT("nuclearcraft"),
    BLOODMAGIC("bloodmagic");

    public final String modid;
    private final boolean loaded;

    Mods(String modName) {
        this.modid = modName;
        this.loaded = Loader.isModLoaded(this.modid);
    }

    public boolean isPresent() {
        return loaded;
    }
}
