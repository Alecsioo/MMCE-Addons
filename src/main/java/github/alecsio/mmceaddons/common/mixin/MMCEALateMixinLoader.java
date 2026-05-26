package github.alecsio.mmceaddons.common.mixin;

import net.minecraftforge.fml.common.Loader;
import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class MMCEALateMixinLoader implements ILateMixinLoader {

    @Override
    public List<String> getMixinConfigs() {
        return Arrays.asList(
                "mixins.mmcea_iceandfire.json",
                "mixins.mmcea_modularmachinery.json",
                "mixins.mmcea_nuclearcraft.json"
        );
    }

    @Override
    public boolean shouldMixinConfigQueue(final String mixinConfig) {
        return switch (mixinConfig) {
            case "mixins.mmcea_modularmachinery.json" -> Loader.isModLoaded("modularmachinery");
            case "mixins.mmcea_iceandfire.json" -> Loader.isModLoaded("iceandfire");
            case "mixins.mmcea_nuclearcraft.json" -> Loader.isModLoaded("nuclearcraft");
            default -> true;
        };
    }
}
