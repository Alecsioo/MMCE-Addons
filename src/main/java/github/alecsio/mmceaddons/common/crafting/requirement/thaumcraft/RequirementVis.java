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

    public static RequirementVis from(IOType ioType, int chunkRange, float amount) {
        requirementValidator.validateNotNegative(chunkRange, "chunkRange");
        requirementValidator.validateNotNegative(amount, "amount");

        return new RequirementVis(ioType, chunkRange, amount);
    }

    private RequirementVis(IOType actionType, int chunkRange, double amount) {
        super((RequirementTypeVis) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_VIS), actionType);
        this.vis = new Vis((float) amount, chunkRange);
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
        ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> getVisHandler(component).handle(this));
        return true;
    }

    @Override
    public ComponentRequirement<Vis, RequirementTypeVis> deepCopy() {
        return new RequirementVis(this.actionType, this.vis.getChunkRange(), this.vis.getAmount());
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
    public int getChunkRange() {
        return this.vis.getChunkRange();
    }

    @Override
    public double getAmount() {
        return this.vis.getAmount();
    }

    @Override
    public double getMinPerChunk() {
        return 0;
    }

    @Override
    public double getMaxPerChunk() {
        return Float.MAX_VALUE;
    }
}
