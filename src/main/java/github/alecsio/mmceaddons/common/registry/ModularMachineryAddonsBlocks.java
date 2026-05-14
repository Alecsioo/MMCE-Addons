package github.alecsio.mmceaddons.common.registry;


import github.alecsio.mmceaddons.common.hatch.vanilla.BlockBiomeProviderInput;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockDimensionProviderInput;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockSingularityItemInputBus;
import github.alecsio.mmceaddons.common.hatch.vanilla.BlockSingularityItemOutputBus;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.block.BlockPotentialEnergyProviderInput;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.block.BlockPotentialEnergyProviderOutput;
import github.alecsio.mmceaddons.common.hatch.ae2.block.BlockMEEssentiaInputBus;
import github.alecsio.mmceaddons.common.hatch.ae2.block.BlockMEEssentiaOutputBus;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.block.BlockMeteorProviderOutput;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.block.BlockWillMultiChunkProviderInput;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.block.BlockWillMultiChunkProviderOutput;
import github.alecsio.mmceaddons.common.hatch.iceandfire.block.BlockDragonBreathInput;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.block.BlockRadiationProviderInput;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.block.BlockRadiationProviderOutput;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.block.scrubber.BlockScrubberProviderInput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.block.BlockFluxProviderInput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.block.BlockFluxProviderOutput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.block.BlockVisProviderInput;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.block.BlockVisProviderOutput;

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

    public static BlockSingularityItemInputBus blockSingularityItemInput;
    public static BlockSingularityItemOutputBus blockSingularityItemOutput;

    public static void initialise() {
    }
}
