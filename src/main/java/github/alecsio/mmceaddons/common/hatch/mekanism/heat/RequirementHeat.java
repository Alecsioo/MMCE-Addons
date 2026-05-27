package github.alecsio.mmceaddons.common.hatch.mekanism.heat;

import github.alecsio.mmceaddons.common.hatch.AbstractMultiComponentRequirement;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentHeat;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Heat;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

import javax.annotation.Nonnull;

public class RequirementHeat extends AbstractMultiComponentRequirement<Heat, RequirementTypeHeat> {

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

    @Override
    public ComponentRequirement<Heat, RequirementTypeHeat> deepCopy() {
        return new RequirementHeat(this.getActionType(), new Heat(heat.temperature()));
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
}
