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
                "mixins.mmcea.json"
        );
    }

    @Override
    public boolean shouldMixinConfigQueue(final String mixinConfig) {
        return switch (mixinConfig) {
            case "mixins.mmcea.json" -> Loader.isModLoaded("bloodmagic") && Loader.isModLoaded("modularmachinery");
            default -> true;
        };
    }
}
