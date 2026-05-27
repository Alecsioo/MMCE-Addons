package github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation;

import github.alecsio.mmceaddons.common.hatch.AbstractMultiComponentRequirement;
import github.alecsio.mmceaddons.common.hatch.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentRadiation;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Radiation;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

import javax.annotation.Nonnull;

public class RequirementRadiation extends AbstractMultiComponentRequirement<Radiation, RequirementTypeRadiation> implements IMultiChunkRequirement, IRequirementRadiation {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final int chunkRange;
    private final double amount;
    private final double minPerChunk;
    private final double maxPerChunk;

    public static RequirementRadiation from(IOType actionType, int chunkRange, double amount) {
        requirementValidator.validateNotNegative(chunkRange, "Chunk range must be a positive number!");
        requirementValidator.validateNotNegative(amount, "Amount must be a positive number!");

        return new RequirementRadiation(actionType, chunkRange, amount, 0, Double.MAX_VALUE);
    }

    private RequirementRadiation(IOType actionType, int chunkRange, double amount, double minPerChunk, double maxPerChunk) {
        super((RequirementTypeRadiation) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_RADIATION), actionType);
        this.chunkRange = chunkRange;
        this.amount = amount;
        this.minPerChunk = minPerChunk;
        this.maxPerChunk = maxPerChunk;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentRadiation &&
                cmp instanceof MachineComponentRadiationProvider &&
                cmp.ioType == getActionType();
    }

    @Override
    public ComponentRequirement<Radiation, RequirementTypeRadiation> deepCopy() {
        return this;
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.radiation";
    }

    @Override
    public JEIComponent<Radiation> provideJEIComponent() {
        return new JEIComponentRadiation(this, false);
    }

    @Override
    public IOType getIOType() {
        return actionType;
    }

    @Override
    public int getChunkRange() {
        return chunkRange;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public IOType getType() {
        return actionType;
    }

    @Override
    public double getMinPerChunk() {
        return minPerChunk;
    }

    @Override
    public double getMaxPerChunk() {
        return maxPerChunk;
    }
}
