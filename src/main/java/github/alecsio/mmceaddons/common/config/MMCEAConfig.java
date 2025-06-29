package github.alecsio.mmceaddons.common.config;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import net.minecraftforge.common.config.Config;

@Config(modid = ModularMachineryAddons.MODID)
public class MMCEAConfig {

    @Config.Comment("The cooldown between multiblock assemblies. Default: 1 second")
    public static int cooldown = 1;

    @Config.Comment("The amount of blocks that should be processed during multiblock assembly, per tick. Default: 4")
    public static int blocksProcessedPerTick = 16;

}
