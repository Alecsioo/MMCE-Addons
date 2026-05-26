package github.alecsio.mmceaddons.common.hatch.mekanism.heat;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentHeat;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Heat;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RequirementHeat extends ComponentRequirement.MultiComponentRequirement<Heat, RequirementTypeHeat> {

    private static final RequirementValidator REQUIREMENT_VALIDATOR = RequirementValidator.getInstance();

    private final Heat heat;

    public RequirementHeat(IOType actionType, Heat heat) {
        super((RequirementTypeHeat) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_HEAT), actionType);
        this.heat = heat;
    }

    public static RequirementHeat from(IOType actionType, double heat) {
        REQUIREMENT_VALIDATOR.validateNotNegative(heat, "heat");
        return new RequirementHeat(actionType, new Heat(heat));
    }

    public double getTemperature() {
        return heat.temperature();
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentType &&
                cmp instanceof MachineComponentHeatProvider &&
                cmp.ioType == getActionType();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context) {
        List<IAsyncRequirementHandler<RequirementHeat>> handlers = getHeatHandlers(components);

        CraftCheck check = CraftCheck.failure("error.modularmachineryaddons.requirement.missing.laser.input");
        for (IAsyncRequirementHandler<RequirementHeat> handler : handlers) {
            check = handler.canHandle(this);

            if (check.isSuccess()) {
                return check;
            }
        }

        return check;
    }

    @Override
    public void startCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.INPUT) {
            List<IAsyncRequirementHandler<RequirementHeat>> handlers = getHeatHandlers(components);

            for (IAsyncRequirementHandler<RequirementHeat> handler : handlers) {
                if (handler.canHandle(this).isSuccess()) {
                    handler.handle(this);
                    return;
                }
            }
        }
    }

    @Override
    public void finishCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.OUTPUT) {
            List<IAsyncRequirementHandler<RequirementHeat>> handlers = getHeatHandlers(components);

            for (IAsyncRequirementHandler<RequirementHeat> handler : handlers) {
                if (handler.canHandle(this).isSuccess()) {
                    handler.handle(this);
                    return;
                }
            }
        }
    }

    @Override
    public ComponentRequirement<Heat, RequirementTypeHeat> deepCopy() {
        return new RequirementHeat(this.getActionType(), new Heat(heat.temperature()));
    }

    @Override
    public ComponentRequirement<Heat, RequirementTypeHeat> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.heat";
    }

    @Override
    public JEIComponent<Heat> provideJEIComponent() {
        return new JEIComponentHeat(heat);
    }

    @SuppressWarnings("unchecked")
    private List<IAsyncRequirementHandler<RequirementHeat>> getHeatHandlers(List<ProcessingComponent<?>> components) {
        return Lists.transform(components, component -> component != null ? (IAsyncRequirementHandler<RequirementHeat>) component.getProvidedComponent() : null);
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
}
