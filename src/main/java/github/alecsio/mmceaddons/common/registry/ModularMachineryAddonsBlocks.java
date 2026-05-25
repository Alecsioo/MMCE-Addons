package github.alecsio.mmceaddons.common.registry;


import github.alecsio.mmceaddons.common.hatch.mekanism.laser.BlockLaserInput;
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

public class ModularMachineryAddonsBlocks {

    // Biome / Dimension
    public static BlockBiomeProviderInput blockBiomeProviderInput;
    public static BlockDimensionProviderInput blockDimensionProviderInput;

    // Radiation
    public static BlockRadiationProviderInput blockRadiationProviderInput;
    public static BlockRadiationProviderOutput blockRadiationProviderOutput;
    public static BlockScrubberProviderInput blockScrubberProviderInput;

    // Will
    public static BlockWillMultiChunkProviderInput blockWillMultiChunkProviderInput;
    public static BlockWillMultiChunkProviderOutput blockWillMultiChunkProviderOutput;

    // Meteor
    public static BlockMeteorProviderOutput blockMeteorProviderOutput;

    // Thaumic Energistics
    public static BlockMEEssentiaInputBus blockMEEssentiaInputBus;
    public static BlockMEEssentiaOutputBus blockMEEssentiaOutputBus;

    // Thaumcraft
    public static BlockFluxProviderInput blockFluxProviderInput;
    public static BlockFluxProviderOutput blockFluxProviderOutput;

    public static BlockVisProviderInput blockVisProviderInput;
    public static BlockVisProviderOutput blockVisProviderOutput;

    // Abyssalcraft
    public static BlockPotentialEnergyProviderInput blockPotentialEnergyProviderInput;
    public static BlockPotentialEnergyProviderOutput blockPotentialEnergyProviderOutput;

    // Ice & Fire
    public static BlockDragonBreathInput blockDragonBreathProviderInput;

    // Mekanism
    public static BlockLaserInput blockLaserProviderInput;

    public static BlockSingularityItemInputBus blockSingularityItemInput;
    public static BlockSingularityItemOutputBus blockSingularityItemOutput;

    public static void initialise() {
    }
}
