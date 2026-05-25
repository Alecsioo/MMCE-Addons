package github.alecsio.mmceaddons.common.registry;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.mekanism.laser.BlockLaserInput;
import github.alecsio.mmceaddons.common.hatch.mekanism.laser.TileLaserProvider;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockBiomeProviderInput;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockDimensionProviderInput;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockSingularityItemInputBus;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockSingularityItemOutputBus;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.block.BlockPotentialEnergyProviderInput;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.block.BlockPotentialEnergyProviderOutput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia.BlockMEEssentiaInputBus;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia.BlockMEEssentiaOutputBus;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor.BlockMeteorProviderOutput;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.will.BlockWillMultiChunkProviderInput;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.will.BlockWillMultiChunkProviderOutput;
import github.alecsio.mmceaddons.common.hatch.iceandfire.BlockDragonBreathInput;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation.BlockRadiationProviderInput;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation.BlockRadiationProviderOutput;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.BlockScrubberProviderInput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.flux.BlockFluxProviderInput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.flux.BlockFluxProviderOutput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.vis.BlockVisProviderInput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.vis.BlockVisProviderOutput;
import github.alecsio.mmceaddons.common.hatch.vanilla.TileBiomeProvider;
import github.alecsio.mmceaddons.common.hatch.vanilla.TileDimensionProvider;
import github.alecsio.mmceaddons.common.hatch.vanilla.TileSingularityItemInputBus;
import github.alecsio.mmceaddons.common.hatch.vanilla.TileSingularityItemOutputBus;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.TilePotentialEnergyProvider;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor.TileMeteorProvider;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.will.TileWillMultiChunkProvider;
import github.alecsio.mmceaddons.common.hatch.iceandfire.TileDragonBreathProvider;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation.TileRadiationProvider;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.TileScrubberProvider;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia.MEEssentiaInputBus;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia.MEEssentiaOutputBus;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.flux.TileFluxProvider;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.vis.TileVisProvider;
import github.kasuminova.mmce.common.block.appeng.BlockMEMachineComponent;
import hellfirepvp.modularmachinery.common.block.BlockCustomName;
import hellfirepvp.modularmachinery.common.block.BlockMachineComponent;
import hellfirepvp.modularmachinery.common.item.ItemBlockCustomName;
import hellfirepvp.modularmachinery.common.item.ItemBlockMEMachineComponent;
import hellfirepvp.modularmachinery.common.item.ItemBlockMachineComponent;
import hellfirepvp.modularmachinery.common.item.ItemBlockMachineComponentCustomName;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.List;

// This class was adapted from a similar class in MMCE
public class RegistryBlocks {
    private static final List<Block> blockModelRegister = new ArrayList<>();

    public static final List<Block> BLOCKS = new ArrayList<>();

    public static void initialise() {
        registerBlocks();
        registerTileEntities();
        registerBlockModels();
    }

    private static void registerBlocks() {
        // Biome and Dimension: always registered
        ModularMachineryAddonsBlocks.blockBiomeProviderInput = prepareRegister(new BlockBiomeProviderInput());
        ModularMachineryAddonsBlocks.blockDimensionProviderInput = prepareRegister(new BlockDimensionProviderInput());

        // NuclearCraft-related blocks
        if (Mods.NUCLEARCRAFT.isPresent()) {
            ModularMachineryAddonsBlocks.blockRadiationProviderInput = prepareRegister(new BlockRadiationProviderInput());
            ModularMachineryAddonsBlocks.blockRadiationProviderOutput = prepareRegister(new BlockRadiationProviderOutput());
            ModularMachineryAddonsBlocks.blockScrubberProviderInput = prepareRegister(new BlockScrubberProviderInput());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockRadiationProviderInput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockRadiationProviderOutput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockScrubberProviderInput);
        }

        // Blood Magic (Will & meteor)
        if (Mods.BLOODMAGIC.isPresent()) {
            ModularMachineryAddonsBlocks.blockWillMultiChunkProviderInput = prepareRegister(new BlockWillMultiChunkProviderInput());
            ModularMachineryAddonsBlocks.blockWillMultiChunkProviderOutput = prepareRegister(new BlockWillMultiChunkProviderOutput());
            ModularMachineryAddonsBlocks.blockMeteorProviderOutput = prepareRegister(new BlockMeteorProviderOutput());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockWillMultiChunkProviderInput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockWillMultiChunkProviderOutput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockMeteorProviderOutput);
        }

        // Thaumic Energistics (ME Essentia)
        if (Mods.THAUMICENERGISTICS.isPresent()) {
            ModularMachineryAddonsBlocks.blockMEEssentiaInputBus = prepareRegister(new BlockMEEssentiaInputBus());
            ModularMachineryAddonsBlocks.blockMEEssentiaOutputBus = prepareRegister(new BlockMEEssentiaOutputBus());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockMEEssentiaInputBus);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockMEEssentiaOutputBus);
        }

        // Thaumcraft (Flux & Vis)
        if (Mods.THAUMCRAFT.isPresent()) {
            ModularMachineryAddonsBlocks.blockFluxProviderInput = prepareRegister(new BlockFluxProviderInput());
            ModularMachineryAddonsBlocks.blockFluxProviderOutput = prepareRegister(new BlockFluxProviderOutput());
            ModularMachineryAddonsBlocks.blockVisProviderInput = prepareRegister(new BlockVisProviderInput());
            ModularMachineryAddonsBlocks.blockVisProviderOutput = prepareRegister(new BlockVisProviderOutput());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockFluxProviderInput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockFluxProviderOutput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockVisProviderInput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockVisProviderOutput);
        }

        if (Mods.ABYSSALCRAFT.isPresent()) {
            ModularMachineryAddonsBlocks.blockPotentialEnergyProviderInput = prepareRegister(new BlockPotentialEnergyProviderInput());
            ModularMachineryAddonsBlocks.blockPotentialEnergyProviderOutput = prepareRegister(new BlockPotentialEnergyProviderOutput());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockPotentialEnergyProviderInput);
            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockPotentialEnergyProviderOutput);
        }

        if (Mods.ICE_AND_FIRE.isPresent()) {
            ModularMachineryAddonsBlocks.blockDragonBreathProviderInput = prepareRegister(new BlockDragonBreathInput());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockDragonBreathProviderInput);
        }

        if (Mods.MEKANISM.isPresent()) {
            ModularMachineryAddonsBlocks.blockLaserProviderInput = prepareRegister(new BlockLaserInput());

            prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockLaserProviderInput);
        }

        // Always register
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockBiomeProviderInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockDimensionProviderInput);

        // Singularity Item Buses: always registered:
        ModularMachineryAddonsBlocks.blockSingularityItemInput = prepareRegister(new BlockSingularityItemInputBus());
        ModularMachineryAddonsBlocks.blockSingularityItemOutput = prepareRegister(new BlockSingularityItemOutputBus());
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockSingularityItemInput);
        prepareItemBlockRegister(ModularMachineryAddonsBlocks.blockSingularityItemOutput);

    }

    private static void registerTileEntities() {
        if (Mods.NUCLEARCRAFT.isPresent()) {
            registerTileEntity(TileRadiationProvider.Input.class);
            registerTileEntity(TileRadiationProvider.Output.class);
            registerTileEntity(TileScrubberProvider.class);
        }

        if (Mods.BLOODMAGIC.isPresent()) {
            registerTileEntity(TileWillMultiChunkProvider.Input.class);
            registerTileEntity(TileWillMultiChunkProvider.Output.class);
            registerTileEntity(TileMeteorProvider.Output.class);
        }

        if (Mods.THAUMICENERGISTICS.isPresent()) {
            registerTileEntity(MEEssentiaInputBus.class);
            registerTileEntity(MEEssentiaOutputBus.class);
        }

        if (Mods.THAUMCRAFT.isPresent()) {
            registerTileEntity(TileFluxProvider.Input.class);
            registerTileEntity(TileFluxProvider.Output.class);
            registerTileEntity(TileVisProvider.Input.class);
            registerTileEntity(TileVisProvider.Output.class);
        }

        if (Mods.ABYSSALCRAFT.isPresent()) {
            registerTileEntity(TilePotentialEnergyProvider.Input.class);
            registerTileEntity(TilePotentialEnergyProvider.Output.class);
        }

        if (Mods.ICE_AND_FIRE.isPresent()) {
            registerTileEntity(TileDragonBreathProvider.class);
        }

        if (Mods.MEKANISM.isPresent()) {
            registerTileEntity(TileLaserProvider.class);
        }

        // Always present
        registerTileEntity(TileBiomeProvider.class);
        registerTileEntity(TileDimensionProvider.class);
        registerTileEntity(TileSingularityItemInputBus.class);
        registerTileEntity(TileSingularityItemOutputBus.class);
    }

    /**
     * Builds the tile path given the class name. It removes the package name and concatenates the base class name with the nested, static class' name.
     * For example:
     *  main class: TileRadiationProvider
     *  nested class: Input
     *  canonical name: {packageName}.TileRadiationProvider$Input
     *  output: tileradiationproviderinput
     */
    private static String buildPathForClass(Class<? extends TileEntity> clazz) {
        return clazz.getCanonicalName().replace(clazz.getPackage().getName(), "").replace(".", "").toLowerCase();
    }

    private static void registerTileEntity(Class<? extends TileEntity> entityClass) {
        ModularMachineryAddons.logger.info("Registering TileEntity: " + entityClass);
        GameRegistry.registerTileEntity(entityClass, new ResourceLocation(ModularMachineryAddons.MODID, buildPathForClass(entityClass)));
    }

    private static void registerBlockModels() {
        for (Block block : blockModelRegister) {
            ModularMachineryAddons.proxy.registerBlockModel(block); // If on client side, will call ClientProxy.registerBlockModel
        }
    }

    // Copied from the MMCE code, credits to the original authors
    private static void prepareItemBlockRegister(Block block) {
        if (block instanceof BlockMachineComponent) {
            if (block instanceof BlockMEMachineComponent) {
                prepareItemBlockRegister(new ItemBlockMEMachineComponent(block));
            } else if (block instanceof BlockCustomName) {
                prepareItemBlockRegister(new ItemBlockMachineComponentCustomName(block));
            } else {
                prepareItemBlockRegister(new ItemBlockMachineComponent(block));
            }
        } else {
            if (block instanceof BlockCustomName) {
                prepareItemBlockRegister(new ItemBlockCustomName(block));
            } else {
                prepareItemBlockRegister(new ItemBlock(block));
            }
        }
    }

    // Copied from the MMCE code, credits to the original authors
    private static <T extends ItemBlock> void prepareItemBlockRegister(T item) {
        String name = item.getBlock().getClass().getSimpleName().toLowerCase();
        item.setRegistryName(ModularMachineryAddons.MODID, name).setTranslationKey(ModularMachineryAddons.MODID + '.' + name);

        ModularMachineryAddons.REGISTRY_ITEMS.registerItemBlock(item);
    }

    // Copied from the MMCE code, credits to the original authors
    private static <T extends Block> T prepareRegister(T block) {
        String name = block.getClass().getSimpleName().toLowerCase();
        block.setRegistryName(ModularMachineryAddons.MODID, name).setTranslationKey(ModularMachineryAddons.MODID + '.' + name);
        BLOCKS.add(block);

        return prepareRegisterWithCustomName(block);
    }

    // Copied from the MMCE code, credits to the original authors
    private static <T extends Block> T prepareRegisterWithCustomName(T block) {
        blockModelRegister.add(block);
        return block;
    }

}
