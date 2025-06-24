package github.alecsio.mmceaddons;

import github.alecsio.mmceaddons.common.LoadedModsCache;
import github.alecsio.mmceaddons.common.lib.ModularMachineryAddonsBlocks;
import github.alecsio.mmceaddons.common.registry.internal.EventHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import javax.annotation.Nonnull;

/**
 * CommonProxy handles shared mod-side logic for the server and client.
 */
public class CommonProxy {
    public static CreativeTabs creativeTabModularMachineryAddons;

    private static boolean preInitCalled = false;

    public void preInit(FMLPreInitializationEvent event) {
        // Prevent duplicate calls
        if (preInitCalled) return;
        preInitCalled = true;

        ModularMachineryAddons.logger.info("CommonProxy: Running preInit");

        creativeTabModularMachineryAddons = new CreativeTabs(ModularMachineryAddons.MODID) {
            @Override
            @Nonnull
            public ItemStack createIcon() {
                return new ItemStack(ModularMachineryAddonsBlocks.blockRadiationProviderInput);
            }
        };

        MinecraftForge.EVENT_BUS.register(new EventHandler());

        ModularMachineryAddonsBlocks.initialise();
        LoadedModsCache.initialize();
    }



    // Optional methods to register models; these would be overridden on the client side
    public void registerItemModel(net.minecraft.item.Item item) {}

    public void registerBlockModel(net.minecraft.block.Block block) {}
}