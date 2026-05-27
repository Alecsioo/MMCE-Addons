package github.alecsio.mmceaddons.common.hatch;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMultiComponentRequirement<T, V extends RequirementType<T, ? extends ComponentRequirement<T, V>>> extends ComponentRequirement.MultiComponentRequirement<T, V>{

    public AbstractMultiComponentRequirement(V requirementType, IOType actionType) {
        super(requirementType, actionType);
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(List<ProcessingComponent<?>> list, RecipeCraftingContext recipeCraftingContext) {
        List<IAsyncRequirementHandler<AbstractMultiComponentRequirement<T, V>>> handlers = getHandlers(list);

        CraftCheck recipeCheck = CraftCheck.failure("");

        for (IAsyncRequirementHandler<AbstractMultiComponentRequirement<T, V>> handler : handlers) {
            recipeCheck = handler.canHandle(this);
            if (recipeCheck != null && recipeCheck.isSuccess()) {
                return recipeCheck;
            }
        }

        return recipeCheck;
    }

    @Override
    public void startCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() != IOType.INPUT) {return;}

        handle(components);

        super.startCrafting(components, context, chance);
    }

    @Override
    public void finishCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() != IOType.OUTPUT) {return;}

        handle(components);

        super.finishCrafting(components, context, chance);
    }

    @Override
    public ComponentRequirement<T, V> deepCopyModified(List<RecipeModifier> list) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public List<ProcessingComponent<?>> copyComponents(List<ProcessingComponent<?>> toCopy) {
        List<ProcessingComponent<?>> copy = new ArrayList<>(toCopy.size());

        toCopy.forEach(component -> {
            ProcessingComponent<?> cmp = new ProcessingComponent<>((MachineComponent<Object>) component.getComponent(), component.getProvidedComponent(), component.getTag());
            copy.add(cmp);
        });

        return copy;
    }

    private void handle(List<ProcessingComponent<?>> components) {
        List<IAsyncRequirementHandler<AbstractMultiComponentRequirement<T, V>>> handlers = getHandlers(components);

        for(IAsyncRequirementHandler<AbstractMultiComponentRequirement<T, V>> handler : handlers) {
            CraftCheck recipeCheck = handler.canHandle(this);
            if (recipeCheck != null && recipeCheck.isSuccess()) {
                handler.handle(this);
                return;
            }
        }

        String warning = String.format("Failed to find a handler for %s, action type: %s", this.getClass().getSimpleName(), this.getActionType());
        ModularMachineryAddons.logger.warn(warning);
    }

    private List<IAsyncRequirementHandler<AbstractMultiComponentRequirement<T, V>>> getHandlers(List<ProcessingComponent<?>> components) {
        return Lists.transform(components, component -> component != null ? (IAsyncRequirementHandler<AbstractMultiComponentRequirement<T, V>>) component.getProvidedComponent() : null);
    }
}
