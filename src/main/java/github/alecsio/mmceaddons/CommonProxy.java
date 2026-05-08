package github.alecsio.mmceaddons;

import github.alecsio.mmceaddons.common.gui.ContainerSingularityItemBus;
import github.alecsio.mmceaddons.common.lib.ModularMachineryAddonsBlocks;
import github.alecsio.mmceaddons.common.registry.internal.EventHandler;
import github.alecsio.mmceaddons.common.tile.TileSingularityItemBus;
import hellfirepvp.modularmachinery.common.tiles.base.TileItemBus;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * CommonProxy handles shared mod-side logic for the server and client.
 */
public class CommonProxy implements IGuiHandler {
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

        NetworkRegistry.INSTANCE.registerGuiHandler(ModularMachineryAddons.MODID, this);


        ModularMachineryAddonsBlocks.initialise();
    }

    public void init() {}

    // Optional methods to register models; these would be overridden on the client side
    public void registerItemModel(net.minecraft.item.Item item) {}

    public void registerBlockModel(net.minecraft.block.Block block) {}

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        GuiType type = CommonProxy.GuiType.values()[MathHelper.clamp(ID, 0, CommonProxy.GuiType.values().length - 1)];
        Class<? extends TileEntity> required = type.requiredTileEntity;
        TileEntity present = null;
        if (required != null) {
            TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
            if (te == null || !required.isAssignableFrom(te.getClass())) {
                return null;
            }

            present = te;
        }

        switch (type) {
            case VACUUM_INVENTORY:
                return new ContainerSingularityItemBus((TileItemBus) present, player);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }


    public enum GuiType {

        VACUUM_INVENTORY(TileSingularityItemBus.class);

        public final Class<? extends TileEntity> requiredTileEntity;

        GuiType(@Nullable Class<? extends TileEntity> requiredTileEntity) {
            this.requiredTileEntity = requiredTileEntity;
        }
    }
}