package github.alecsio.mmceaddons.common.integration.crafttweaker;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ZenRegister;
import github.alecsio.mmceaddons.common.hatch.iceandfire.DragonType;
import github.alecsio.mmceaddons.common.hatch.vanilla.RequirementBiome;
import github.alecsio.mmceaddons.common.hatch.vanilla.RequirementDimension;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.RequirementPotentialEnergy;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.RequirementWillMultiChunk;
import github.alecsio.mmceaddons.common.hatch.iceandfire.RequirementDragonBreath;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.RequirementRadiation;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.RequirementScrubber;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.RequirementFlux;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.RequirementVis;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.RequirementEssentia;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.TileFluxProvider;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.TileVisProvider;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.integration.crafttweaker.RecipePrimer;
import hellfirepvp.modularmachinery.common.machine.IOType;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenRegister
@ZenExpansion("mods.modularmachinery.RecipePrimer")
@SuppressWarnings("unused")
public class AddonsPrimer {

    @FunctionalInterface
    private interface RequirementSupplier<T extends ComponentRequirement<?, ?>> {
        T get() throws RequirementPrerequisiteFailedException;
    }

    private static <T extends ComponentRequirement<?, ?>> RecipePrimer addRequirement(RecipePrimer primer, RequirementSupplier<T> supplier) {
        try {
            primer.appendComponent(supplier.get());
        } catch (RequirementPrerequisiteFailedException e) {
            CraftTweakerAPI.logError(e.getMessage());
        }
        return primer;
    }

    @ZenMethod
    public static RecipePrimer addScrubber(RecipePrimer primer, int chunkRange) {
        return addRequirement(primer, () -> RequirementScrubber.from(chunkRange));
    }

    @ZenMethod
    public static RecipePrimer addRadiationInput(RecipePrimer primer, int chunkRange, int amount) {
        return addRequirement(primer, () -> RequirementRadiation.from(IOType.INPUT, chunkRange, amount));
    }

    @ZenMethod
    public static RecipePrimer addRadiationOutput(RecipePrimer primer, int chunkRange, int amount) {
        return addRequirement(primer, () -> RequirementRadiation.from(IOType.OUTPUT, chunkRange, amount));
    }

    @ZenMethod
    public static RecipePrimer addWillInput(RecipePrimer primer, int chunkRange, int amount, int minPerChunk, int maxPerChunk, String willType) {
        return addRequirement(primer, () -> RequirementWillMultiChunk.from(IOType.INPUT, chunkRange, amount, minPerChunk, maxPerChunk, willType));
    }

    @ZenMethod
    public static RecipePrimer addWillInput(RecipePrimer primer, int amount, int minPerChunk, int maxPerChunk, String willType) {
        return addWillInput(primer, 0, amount, minPerChunk, maxPerChunk, willType);
    }

    @ZenMethod
    public static RecipePrimer addWillInput(RecipePrimer primer, int amount, String willType) {
        return addWillInput(primer, 0, amount, 0, Integer.MAX_VALUE, willType);
    }

    @ZenMethod
    public static RecipePrimer addWillOutput(RecipePrimer primer, int chunkRange, int amount, int minPerChunk, int maxPerChunk, String willType) {
        return addRequirement(primer, () -> RequirementWillMultiChunk.from(IOType.OUTPUT, chunkRange, amount, minPerChunk, maxPerChunk, willType));
    }

    @ZenMethod
    public static RecipePrimer addWillOutput(RecipePrimer primer, int amount, int minPerChunk, int maxPerChunk, String willType) {
        return addWillOutput(primer, 0, amount, minPerChunk, maxPerChunk, willType);
    }

    @ZenMethod
    public static RecipePrimer addWillOutput(RecipePrimer primer, int amount, String willType) {
        return addWillOutput(primer, 0, amount, 0, Integer.MAX_VALUE, willType);
    }

    @ZenMethod
    public static RecipePrimer addFluxInput(RecipePrimer primer, float amount, int chunkRange) {
        return addRequirement(primer, () -> RequirementFlux.from(IOType.INPUT, chunkRange, amount));
    }

    @ZenMethod
    public static RecipePrimer addFluxInput(RecipePrimer primer, float amount, int chunkRange, int minPerChunk) {
        return addRequirement(primer, () -> RequirementFlux.from(IOType.INPUT, chunkRange, amount, minPerChunk, TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));
    }

    @ZenMethod
    public static RecipePrimer addFluxInput(RecipePrimer primer, float amount) {
        return addFluxInput(primer, amount, 0);
    }

    @ZenMethod
    public static RecipePrimer addFluxOutput(RecipePrimer primer, float amount, int chunkRange) {
        return addRequirement(primer, () -> RequirementFlux.from(IOType.OUTPUT, chunkRange, amount));
    }

    @ZenMethod
    public static RecipePrimer addFluxOutput(RecipePrimer primer, float amount, int chunkRange, int maxPerChunk) {
        return addRequirement(primer, () -> RequirementFlux.from(IOType.OUTPUT, chunkRange, amount, 0, maxPerChunk));
    }

    @ZenMethod
    public static RecipePrimer addFluxOutput(RecipePrimer primer, float amount) {
        return addFluxOutput(primer, amount, 0);
    }

    @ZenMethod
    public static RecipePrimer addVisInput(RecipePrimer primer, float amount, int chunkRange) {
        return addRequirement(primer, () -> RequirementVis.from(IOType.INPUT, chunkRange, amount));
    }

    @ZenMethod
    public static RecipePrimer addVisInput(RecipePrimer primer, float amount, int chunkRange, int minPerChunk) {
        return addRequirement(primer, () -> RequirementVis.from(IOType.INPUT, chunkRange, amount, minPerChunk, TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));
    }

    @ZenMethod
    public static RecipePrimer addVisInput(RecipePrimer primer, float amount) {
        return addVisInput(primer, amount, 0);
    }

    @ZenMethod
    public static RecipePrimer addVisOutput(RecipePrimer primer, float amount, int chunkRange) {
        return addRequirement(primer, () -> RequirementVis.from(IOType.OUTPUT, chunkRange, amount));
    }

    @ZenMethod
    public static RecipePrimer addVisOutput(RecipePrimer primer, float amount, int chunkRange, int maxPerChunk) {
        return addRequirement(primer, () -> RequirementVis.from(IOType.OUTPUT, chunkRange, amount, 0, maxPerChunk));
    }

    @ZenMethod
    public static RecipePrimer addVisOutput(RecipePrimer primer, float amount) {
        return addVisOutput(primer, amount, 0);
    }

    @ZenMethod
    public static RecipePrimer addEssentiaInput(RecipePrimer primer, String aspect, int amount) {
        return addRequirement(primer, () -> RequirementEssentia.from(IOType.INPUT, aspect, amount));
    }

    @ZenMethod
    public static RecipePrimer addEssentiaOutput(RecipePrimer primer, String aspect, int amount) {
        return addRequirement(primer, () -> RequirementEssentia.from(IOType.OUTPUT, aspect, amount));
    }

    @ZenMethod
    public static RecipePrimer addBiomeInput(RecipePrimer primer, String biomeRegistryName) {
        return addRequirement(primer, () -> RequirementBiome.from(IOType.INPUT, biomeRegistryName));
    }

    @ZenMethod
    public static RecipePrimer addDimensionInput(RecipePrimer primer, int id) {
        return addRequirement(primer, () -> RequirementDimension.from(IOType.INPUT, id));
    }

    @ZenMethod
    public static RecipePrimer addPotentialEnergyInput(RecipePrimer primer, float amount) {
        return addRequirement(primer, () -> RequirementPotentialEnergy.from(IOType.INPUT, amount));
    }

    @ZenMethod
    public static RecipePrimer addPotentialEnergyOutput(RecipePrimer primer, float amount) {
        return addRequirement(primer, () -> RequirementPotentialEnergy.from(IOType.OUTPUT, amount));
    }

    @ZenMethod
    public static RecipePrimer addFireDragonBreathInput(RecipePrimer primer, int amount) {
        return addRequirement(primer, () -> RequirementDragonBreath.from(IOType.INPUT, DragonType.FIRE.name(), amount));
    }

    @ZenMethod
    public static RecipePrimer addIceDragonBreathInput(RecipePrimer primer, int amount) {
        return addRequirement(primer, () -> RequirementDragonBreath.from(IOType.INPUT, DragonType.ICE.name(), amount));
    }

    @ZenMethod
    public static RecipePrimer addLightningDragonBreathInput(RecipePrimer primer, int amount) {
        return addRequirement(primer, () -> RequirementDragonBreath.from(IOType.INPUT, DragonType.LIGHTNING.name(), amount));
    }
}
