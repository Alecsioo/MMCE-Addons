package github.alecsio.mmceaddons.common.hatch.mekanism.laser;

import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentLaser;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Laser;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RequirementLaser extends ComponentRequirement.MultiComponentRequirement<Laser, RequirementTypeLaser> {

    private static final RequirementValidator REQUIREMENT_VALIDATOR = RequirementValidator.getInstance();

    private final Laser laser;

    public RequirementLaser(IOType actionType, Laser laser) {
        super((RequirementTypeLaser) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_LASER), actionType);
        this.laser = laser;
    }

    public static RequirementLaser from(IOType ioType, double power) {
        REQUIREMENT_VALIDATOR.validateNotNegative(power, "power");
        return new RequirementLaser(ioType, new Laser(power));
    }

    public double getPower() {
        return laser.power();
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentType &&
                cmp instanceof MachineComponentLaserProvider &&
                cmp.ioType == getActionType();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context) {
        List<IAsyncRequirementHandler<RequirementLaser>> handlers = getLaserHandlers(components);

        CraftCheck check = CraftCheck.failure("error.modularmachineryaddons.requirement.missing.laser.input");
        for (IRequirementHandler<RequirementLaser> handler : handlers) {
            check = handler.canHandle(this);

            if (check.isSuccess()) {
                return check;
            }
        }

        return check;
    }

    @Override
    public void startCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context, ResultChance chance) {
        List<IAsyncRequirementHandler<RequirementLaser>> handlers = getLaserHandlers(components);

        for (IAsyncRequirementHandler<RequirementLaser> handler : handlers) {
            if (handler.canHandle(this).isSuccess()) {
                handler.handle(this);
                return;
            }
        }
    }

    @Override
    public ComponentRequirement<Laser, RequirementTypeLaser> deepCopy() {
        return new RequirementLaser(this.actionType, new Laser(this.laser.power()));
    }

    @Override
    public ComponentRequirement<Laser, RequirementTypeLaser> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.laser";
    }

    @Override
    public JEIComponent<Laser> provideJEIComponent() {
        return new JEIComponentLaser(laser);
    }

    @SuppressWarnings("unchecked")
    private List<IAsyncRequirementHandler<RequirementLaser>> getLaserHandlers(List<ProcessingComponent<?>> components) {
        return Lists.transform(components, component -> component != null ? (IAsyncRequirementHandler<RequirementLaser>) component.getProvidedComponent() : null);
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
