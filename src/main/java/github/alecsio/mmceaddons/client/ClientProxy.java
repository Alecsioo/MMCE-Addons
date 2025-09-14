package github.alecsio.mmceaddons.client;

import github.alecsio.mmceaddons.CommonProxy;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.client.render.entity.MeteorRenderFactory;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.entity.EntityImprovedMeteor;
import hellfirepvp.modularmachinery.common.block.BlockDynamicColor;
import hellfirepvp.modularmachinery.common.item.ItemDynamicColor;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = ModularMachineryAddons.MODID)
public class ClientProxy extends CommonProxy {

    private static final List<Item> ITEM_MODELS_TO_REGISTER = new ArrayList<>();
    private static final List<Block> BLOCK_MODELS_TO_REGISTER = new ArrayList<>();

    @Override
    public void preInit(FMLPreInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(this);
        KeyBindings.init();

        if (Mods.BLOODMAGIC.isPresent()) {
            RenderingRegistry.registerEntityRenderingHandler(EntityImprovedMeteor.class, new MeteorRenderFactory());
        }

        super.preInit(event);
    }

    @Override
    public void init() {
        super.init();
        BlockColors blockColors = Minecraft.getMinecraft().getBlockColors();

        for (Block block : BLOCK_MODELS_TO_REGISTER) {
            if (block instanceof BlockDynamicColor blockDynamicColor) {
                blockColors.registerBlockColorHandler(blockDynamicColor::getColorMultiplier, block);
            }
        }

        ItemColors itemColors = Minecraft.getMinecraft().getItemColors();
        for (Item item : ITEM_MODELS_TO_REGISTER) {
            if (item instanceof ItemDynamicColor itemDynamicColor) {
                itemColors.registerItemColorHandler(itemDynamicColor::getColorFromItemstack, item);
            }
        }
    }

    @SubscribeEvent
    public void onModelRegister(ModelRegistryEvent event) {
        registerModels();
    }

    @Override
    public void registerItemModel(Item item) {
        ITEM_MODELS_TO_REGISTER.add(item);
    }

    @Override
    public void registerBlockModel(Block block) {
        BLOCK_MODELS_TO_REGISTER.add(block);
    }

    private void registerModels() {
        registerItemModels();
        registerBlockModels();
    }

    private void registerBlockModels() {
        BLOCK_MODELS_TO_REGISTER.forEach(block -> {
            ModularMachineryAddons.logger.info("Registering block model for " + block.getRegistryName());
            Item item = Item.getItemFromBlock(block);
            ModelBakery.registerItemVariants(item, block.getRegistryName());
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        });
    }

    private void registerItemModels() {
        ITEM_MODELS_TO_REGISTER.forEach(item -> {
            ModularMachineryAddons.logger.info("Registering item model for " + item.getRegistryName());
            ModelBakery.registerItemVariants(item, item.getRegistryName());
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
        });
    }


}
