package github.alecsio.mmceaddons.common.hatch.bloodmagic;

import WayofTime.bloodmagic.soul.EnumDemonWillType;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentWill;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import kport.modularmagic.common.integration.jei.ingredient.DemonWill;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class RequirementWillMultiChunk extends ComponentRequirement<DemonWill, RequirementTypeWillMultiChunk> implements IMultiChunkRequirement {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    public EnumDemonWillType willType;
    private final int chunkRange;
    private final double amount;
    private final double minPerChunk;
    private final double maxPerChunk;

    public static RequirementWillMultiChunk from(IOType ioType, int chunkRange, double amount, double minPerChunk, double maxPerChunk, String willType) {
        requirementValidator.validateNotNegative(amount, "Amount must be a positive number!");
        requirementValidator.validateNotNegative(chunkRange, "Chunk range must be a positive number!");
        requirementValidator.validateNotNegative(minPerChunk, "Minimum per chunk must be a positive number!");
        requirementValidator.validateNotNegative(maxPerChunk, "Max per chunk must be a positive number!");

        EnumDemonWillType will;
        try {
            will = EnumDemonWillType.valueOf(willType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RequirementPrerequisiteFailedException(e.getMessage());
        }
        return new RequirementWillMultiChunk(ioType, chunkRange, amount, minPerChunk, maxPerChunk, will);
    }

    private RequirementWillMultiChunk(IOType actionType, int chunkRange, double amount, double minPerChunk, double maxPerChunk, EnumDemonWillType willType) {
        super((RequirementTypeWillMultiChunk) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_WILL_MULTI_CHUNK), actionType);
        this.chunkRange = chunkRange;
        this.amount = amount;
        this.minPerChunk = minPerChunk;
        this.maxPerChunk = maxPerChunk;
        this.willType = willType;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cpn = component.getComponent();
        return cpn.getContainerProvider() instanceof TileWillMultiChunkProvider &&
                cpn.getComponentType() instanceof ComponentWillMultiChunk &&
                cpn.ioType == getActionType();
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (!canStartCrafting(component, context, Collections.emptyList()).isSuccess()) return false;

        if (getActionType() == IOType.INPUT) {
            ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> getWillHandler(component).handle(this));
        }
        return true;
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.OUTPUT) {
            ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> getWillHandler(component).handle(this));
        }
        return CraftCheck.success();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> restrictions) {
        return getWillHandler(component).canHandle(this);
    }

    @Override
    public ComponentRequirement<DemonWill, RequirementTypeWillMultiChunk> deepCopy() {
        return new RequirementWillMultiChunk(this.actionType, this.chunkRange, this.amount, this.minPerChunk, this.maxPerChunk, this.willType);
    }

    @Override
    public ComponentRequirement<DemonWill, RequirementTypeWillMultiChunk> deepCopyModified(List<RecipeModifier> list) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.will";
    }

    @Override
    public JEIComponent<DemonWill> provideJEIComponent() {
        return new JEIComponentWill(this);
    }

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementWillMultiChunk> getWillHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementWillMultiChunk>) component.getComponent().getContainerProvider();
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
        return this.minPerChunk;
    }

    @Override
    public double getMaxPerChunk() {
        return this.maxPerChunk;
    }
}
