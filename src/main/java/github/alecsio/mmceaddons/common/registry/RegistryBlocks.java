package github.alecsio.mmceaddons.common.registry;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.block.ae2.BlockMEEssentiaInputBus;
import github.alecsio.mmceaddons.common.block.ae2.BlockMEEssentiaOutputBus;
import github.alecsio.mmceaddons.common.block.bloodmagic.BlockMeteorProviderOutput;
import github.alecsio.mmceaddons.common.block.bloodmagic.BlockWillMultiChunkProviderInput;
import github.alecsio.mmceaddons.common.block.bloodmagic.BlockWillMultiChunkProviderOutput;
import github.alecsio.mmceaddons.common.block.nuclearcraft.BlockRadiationProviderInput;
import github.alecsio.mmceaddons.common.block.nuclearcraft.BlockRadiationProviderOutput;
import github.alecsio.mmceaddons.common.block.nuclearcraft.scrubber.BlockScrubberProviderInput;
import github.alecsio.mmceaddons.common.block.thaumcraft.BlockFluxProviderInput;
import github.alecsio.mmceaddons.common.block.thaumcraft.BlockFluxProviderOutput;
import github.alecsio.mmceaddons.common.block.thaumcraft.BlockVisProviderInput;
import github.alecsio.mmceaddons.common.block.thaumcraft.BlockVisProviderOutput;
import github.alecsio.mmceaddons.common.lib.ModularMachineryAddonsBlocks;
import github.alecsio.mmceaddons.common.tile.bloodmagic.TileMeteorProvider;
import github.alecsio.mmceaddons.common.tile.bloodmagic.TileWillMultiChunkProvider;
import github.alecsio.mmceaddons.common.tile.nuclearcraft.TileRadiationProvider;
import github.alecsio.mmceaddons.common.tile.nuclearcraft.TileScrubberProvider;
import github.alecsio.mmceaddons.common.tile.thaumcraft.MEEssentiaInputBus;
import github.alecsio.mmceaddons.common.tile.thaumcraft.MEEssentiaOutputBus;
import github.alecsio.mmceaddons.common.tile.thaumcraft.TileFluxProvider;
import github.alecsio.mmceaddons.common.tile.thaumcraft.TileVisProvider;
import github.kasuminova.mmce.common.block.appeng.BlockMEMachineComponent;
import hellfirepvp.modularmachinery.common.block.BlockCustomName;
import hellfirepvp.modularmachinery.common.block.BlockMachineComponent;
import hellfirepvp.modularmachinery.common.item.ItemBlockCustomName;
import hellfirepvp.modularmachinery.common.item.ItemBlockMEMachineComponent;
import hellfirepvp.modularmachinery.common.item.ItemBlockMachineComponent;
import hellfirepvp.modularmachinery.common.item.ItemBlockMachineComponentCustomName;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

public class RegistryBlocks {
    private static final List<Block> blockModelRegister = new ArrayList<>();

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static void initialise() {
        registerBlocks();
        registerTileEntities();
        registerBlockModels();
    }

    private static void registerBlocks() {
        ModularMachineryAddonsBlocks.blockRadiationProviderInput = prepareRegister(new BlockRadiationProviderInput());
        ModularMachineryAddonsBlocks.blockRadiationProviderOutput = prepareRegister(new BlockRadiationProviderOutput());
        ModularMachineryAddonsBlocks.blockWillMultiChunkProviderInput = prepareRegister(new BlockWillMultiChunkProviderInput());
        ModularMachineryAddonsBlocks.blockWillMultiChunkProviderOutput = prepareRegister(new BlockWillMultiChunkProviderOutput());

        ModularMachineryAddonsBlocks.blockMeteorProviderOutput = prepareRegister(new BlockMeteorProviderOutput());

        ModularMachineryAddonsBlocks.blockMEEssentiaInputBus = prepareRegister(new BlockMEEssentiaInputBus());
        ModularMachineryAddonsBlocks.blockMEEssentiaOutputBus = prepareRegister(new BlockMEEssentiaOutputBus());

        ModularMachineryAddonsBlocks.blockFluxProviderInput = prepareRegister(new BlockFluxProviderInput());
        ModularMachineryAddonsBlocks.blockFluxProviderOutput = prepareRegister(new BlockFluxProviderOutput());

        ModularMachineryAddonsBlocks.blockVisProviderInput = prepareRegister(new BlockVisProviderInput());
        ModularMachineryAddonsBlocks.blockVisProviderOutput = prepareRegister(new BlockVisProviderOutput());

        ModularMachineryAddonsBlocks.blockScrubberProviderInput = prepareRegister(new BlockScrubberProviderInput());

        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockRadiationProviderInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockRadiationProviderOutput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockWillMultiChunkProviderInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockWillMultiChunkProviderOutput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockMeteorProviderOutput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockMEEssentiaInputBus);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockMEEssentiaOutputBus);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockFluxProviderInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockFluxProviderOutput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockScrubberProviderInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockVisProviderInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockVisProviderOutput);
    }

    private static void registerTileEntities() {
        registerTileEntity(TileRadiationProvider.Input.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileRadiationProvider.Input.class)));
        registerTileEntity(TileRadiationProvider.Output.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileRadiationProvider.Output.class)));
        registerTileEntity(TileWillMultiChunkProvider.Input.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileWillMultiChunkProvider.Input.class)));
        registerTileEntity(TileWillMultiChunkProvider.Output.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileWillMultiChunkProvider.Output.class)));
        registerTileEntity(TileMeteorProvider.Output.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileMeteorProvider.Output.class)));
        registerTileEntity(MEEssentiaInputBus.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(MEEssentiaInputBus.class)));
        registerTileEntity(MEEssentiaOutputBus.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(MEEssentiaOutputBus.class)));
        registerTileEntity(TileFluxProvider.Input.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileFluxProvider.Input.class)));
        registerTileEntity(TileFluxProvider.Output.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileFluxProvider.Output.class)));
        registerTileEntity(TileVisProvider.Input.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileVisProvider.Input.class)));
        registerTileEntity(TileVisProvider.Output.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileVisProvider.Output.class)));
        registerTileEntity(TileScrubberProvider.class, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(TileScrubberProvider.class)));
    }

    /**
     * Builds the tile path given the class name. It removes the package name and concatenates the base class name with the nested, static class' name.
     * For example:
     *  main class: TileRadiationProvider
     *  nested class: Input
     *  canonical name: {packageName}.TileRadiationProvider$Input
     *  output: tileradiationproviderinput
     */
    private static String buildPathForClass(Class<? extends MachineComponentTile> clazz) {
        return clazz.getCanonicalName().replace(clazz.getPackage().getName(), "").replace(".", "").toLowerCase();
    }

    private static void registerTileEntity(Class<? extends TileEntity> entityClass, ResourceLocation name) {
        ModularMachineryAddons.logger.info("Registering TileEntity: " + entityClass);
        GameRegistry.registerTileEntity(entityClass, name);
    }

    private static void registerBlockModels() {
        for (Block block : blockModelRegister) {
            ModularMachineryAddons.proxy.registerBlockModel(block); // If on client side, will call ClientProxy.registerBlockModel
        }
    }

    private static ItemBlock prepareItemBlockRegister(Block block) {
        if (block instanceof BlockMachineComponent) {
            if (block instanceof BlockMEMachineComponent) {
                return prepareItemBlockRegister(new ItemBlockMEMachineComponent(block));
            } else if (block instanceof BlockCustomName) {
                return prepareItemBlockRegister(new ItemBlockMachineComponentCustomName(block));
            } else {
                return prepareItemBlockRegister(new ItemBlockMachineComponent(block));
            }
        } else {
            if (block instanceof BlockCustomName) {
                return prepareItemBlockRegister(new ItemBlockCustomName(block));
            } else {
                return prepareItemBlockRegister(new ItemBlock(block));
            }
        }
    }

    private static <T extends ItemBlock> T prepareItemBlockRegister(T item) {
        String name = item.getBlock().getClass().getSimpleName().toLowerCase();
        item.setRegistryName(ModularMachineryAddons.MODID, name).setTranslationKey(ModularMachineryAddons.MODID + '.' + name);

        ModularMachineryAddons.REGISTRY_ITEMS.registerItemBlock(item);
        return item;
    }

    private static <T extends Block> T prepareRegister(T block) {
        String name = block.getClass().getSimpleName().toLowerCase();
        block.setRegistryName(ModularMachineryAddons.MODID, name).setTranslationKey(ModularMachineryAddons.MODID + '.' + name);
        BLOCKS.add(block);

        return prepareRegisterWithCustomName(block);
    }

    private static <T extends Block> T prepareRegisterWithCustomName(T block) {
        blockModelRegister.add(block);
        //CommonProxy.registryPrimer.register(block);

        return block;
    }

}
