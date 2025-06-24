package github.alecsio.mmceaddons.common.config;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import net.minecraftforge.common.config.Config;

@Config(modid = ModularMachineryAddons.MODID)
public class MMCEAConfig {

    @Config.Comment({"The amount of radiation that. Default: 10000"})
    public static int spongeMaxRadiation = 10000;

    @Config.Comment("The cooldown between multiblock assemblies. Default: 1 second")
    public static int cooldown = 1;

    @Config.Comment("The amount of blocks that should be processed during multiblock assembly, per tick. Default: 4")
    public static int assemblyBlocksProcessedPerTick = 4;

    @Config.Comment("The amount of blocks that should be processed during multiblock disassembly, per tick. Default: 4")
    public static int disassemblyBlocksProcessedPerTick = 4;

}
