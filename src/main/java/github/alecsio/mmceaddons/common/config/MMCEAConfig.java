package github.alecsio.mmceaddons.common.config;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ModularMachineryAddons.MODID)
@Config.LangKey("modularmachineryaddons.config.title")
public class MMCEAConfig {

    @Config.Comment({"The amount of radiation that. Default: 10000"})
    public static int spongeMaxRadiation = 10000;

    @Config.Comment("Whether the cooldown for assemblies/disassemblies should be enabled or not. Default: true")
    public static boolean cooldownEnabled = true;

    @Config.Comment("The cooldown between multiblock assemblies. Default: 1 second")
    public static int cooldown = 1;

    @Config.Comment("The amount of blocks that should be processed during multiblock assembly, per tick. Default: 4")
    public static int assemblyBlocksProcessedPerTick = 4;

    @Config.Comment("The amount of blocks that should be processed during multiblock disassembly, per tick. Default: 4")
    public static int disassemblyBlocksProcessedPerTick = 4;

    @Config.Comment("Whether the construct tool should include NBT in the machine output. Default: false")
    public static boolean constructNBT = false;

    @Mod.EventBusSubscriber(modid = ModularMachineryAddons.MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ModularMachineryAddons.MODID)) {
                ConfigManager.sync(ModularMachineryAddons.MODID, Config.Type.INSTANCE);
            }
        }
    }

}
