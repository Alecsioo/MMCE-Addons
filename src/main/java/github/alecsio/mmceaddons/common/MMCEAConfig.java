package github.alecsio.mmceaddons.common;

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

    @Config.Comment("The maximum amount of potential energy that potential energy hatches (input and output) can hold. Default: 4096")
    public static int potentialEnergyHatchesCapacity = 4096;

    @Config.Comment("The maximum amount of energy that laser hatches can hold. Default: 2^31 - 1")
    public static double laserHatchesCapacity = Integer.MAX_VALUE;

    @Config.Comment("The maximum amount of heat that heat hatches can hold. Default: 2^16")
    public static double heatHatchesCapacity = Math.pow(2, 16);

    @Config.Comment("The maximum amount of dragon breath charges that the dragon breath input hatch can hold. Default: 8")
    public static int dragonBreathChargesCapacity = 8;

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
