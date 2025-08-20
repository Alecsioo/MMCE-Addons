package github.alecsio.mmceaddons.common.crafting.requirement.thaumcraft;

import github.alecsio.mmceaddons.common.crafting.component.ComponentVis;
import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.crafting.requirement.types.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.crafting.requirement.types.thaumcraft.RequirementTypeVis;
import github.alecsio.mmceaddons.common.crafting.requirement.validator.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentVis;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Vis;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentVisProvider;
import github.alecsio.mmceaddons.common.tile.thaumcraft.TileVisProvider;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementVis extends ComponentRequirement<Vis, RequirementTypeVis> implements IMultiChunkRequirement {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final Vis vis;

    public static RequirementVis from(IOType ioType, int chunkRange, float amount, float minPerChunk, float maxPerChunk) {
        requirementValidator.validateNotNegative(chunkRange, "Chunk range must be a positive number!");
        requirementValidator.validateNotNegative(amount, "Amount must be a positive number!");
        if (ioType == IOType.OUTPUT) {requirementValidator.validateNotAbove(amount, TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK, String.format("Amount cannot be greater than maximum amount allowed in chunk (this is a TC API limitation): %f", TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));}

        requirementValidator.validateNotNegative(minPerChunk, "Minimum per chunk must be a positive number!");
        requirementValidator.validateNotAbove(maxPerChunk, TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK, String.format("Amount cannot be greater than maximum amount allowed in chunk (this is a TC API limitation): %f", TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK));

        return new RequirementVis(ioType, chunkRange, amount, minPerChunk, maxPerChunk);
    }

    public static RequirementVis from(IOType ioType, int chunkRange, float amount) {
        return from(ioType, chunkRange, amount, 0, TileVisProvider.Output.MAXIMUM_AMOUNT_IN_CHUNK);
    }

    private RequirementVis(IOType actionType, int chunkRange, float amount, float minPerChunk, float maxPerChunk) {
        super((RequirementTypeVis) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_VIS), actionType);
        this.vis = new Vis(amount, chunkRange, minPerChunk, maxPerChunk);
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentVis &&
                cmp instanceof MachineComponentVisProvider &&
                cmp.ioType == getActionType();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> restrictions) {
        return getVisHandler(component).canHandle(this);
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.INPUT) {
            ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> getVisHandler(component).handle(this));
        }
        return true;
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.OUTPUT) {
            ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> getVisHandler(component).handle(this));
        }
        return CraftCheck.success();
    }

    @Override
    public ComponentRequirement<Vis, RequirementTypeVis> deepCopy() {
        return new RequirementVis(this.actionType, this.vis.getChunkRange(), this.vis.getAmount(), this.vis.getMinPerChunk(), this.vis.getMaxPerChunk());
    }

    @Override
    public ComponentRequirement<Vis, RequirementTypeVis> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.vis";
    }

    @Override
    public JEIComponent<Vis> provideJEIComponent() {
        return new JEIComponentVis(this.vis);
    }

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementVis> getVisHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementVis>) component.getComponent().getContainerProvider();
    }

    @Override
    public IOType getIOType() {
        return actionType;
    }

    @Override
    public int getChunkRange() {
        return this.vis.getChunkRange();
    }

    @Override
    public double getAmount() {
        return this.vis.getAmount();
    }

    @Override
    public double getMinPerChunk() {
        return vis.getMinPerChunk();
    }

    @Override
    public double getMaxPerChunk() {
        return vis.getMaxPerChunk();
    }
}
