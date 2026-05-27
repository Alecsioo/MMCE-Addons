package github.alecsio.mmceaddons.common.hatch.thaumcraft.flux;

import github.alecsio.mmceaddons.common.hatch.AbstractMultiComponentRequirement;
import github.alecsio.mmceaddons.common.hatch.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentFlux;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Flux;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

import javax.annotation.Nonnull;

public class RequirementFlux extends AbstractMultiComponentRequirement<Flux, RequirementTypeFlux> implements IMultiChunkRequirement {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final int chunkRange;
    private final double amount;
    private final double minPerChunk;
    private final double maxPerChunk;

    public static RequirementFlux from(IOType ioType, int chunkRange, float amount) {
        requirementValidator.validateNotNegative(chunkRange, "Chunk range must be a positive number!");
        requirementValidator.validateNotNegative(amount, "Amount must be a positive number!");
        if (ioType == IOType.OUTPUT) {requirementValidator.validateNotAbove(amount, TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK, String.format("Amount cannot be greater than maximum amount allowed in chunk (this is a TC API limitation): %f", TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));}

        return new RequirementFlux(ioType, chunkRange, amount, 0, TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK);
    }

    public static RequirementFlux from(IOType ioType, int chunkRange, double amount, double minPerChunk, double maxPerChunk) {
        requirementValidator.validateNotNegative(chunkRange, "Chunk range must be a positive number!");
        requirementValidator.validateNotNegative(amount, "Amount must be a positive number!");
        if (ioType == IOType.OUTPUT) {requirementValidator.validateNotAbove(amount, TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK, String.format("Amount cannot be greater than maximum amount allowed in chunk (this is a TC API limitation): %f", TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));}
        requirementValidator.validateNotNegative(minPerChunk, "Minimum per chunk must be a positive number!");
        requirementValidator.validateNotAbove(maxPerChunk, TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK, String.format("Max per chunk cannot be greater than maximum amount allowed in chunk (this is a TC API limitation): %f", TileFluxProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));

        return new RequirementFlux(ioType, chunkRange, amount, minPerChunk, maxPerChunk);
    }

    public RequirementFlux(IOType actionType, int chunkRange, double amount, double minPerChunk, double maxPerChunk) {
        super((RequirementTypeFlux) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_FLUX), actionType);
        this.chunkRange = chunkRange;
        this.amount = amount;
        this.minPerChunk = minPerChunk;
        this.maxPerChunk = maxPerChunk;
    }

    @Override
    public IOType getIOType() {
        return actionType;
    }

    @Override
    public int getChunkRange() {
        return this.chunkRange;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }

    @Override
    public double getMinPerChunk() {
        return minPerChunk;
    }

    @Override
    public double getMaxPerChunk() {
        return maxPerChunk;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentFlux &&
                cmp instanceof MachineComponentFluxProvider &&
                cmp.ioType == getActionType();
    }

    @Override
    public ComponentRequirement<Flux, RequirementTypeFlux> deepCopy() {
        return new RequirementFlux(getActionType(), chunkRange, amount, minPerChunk, maxPerChunk);
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.flux";
    }

    @Override
    public JEIComponent<Flux> provideJEIComponent() {
        return new JEIComponentFlux(new Flux((float) this.amount, chunkRange));
    }
}
