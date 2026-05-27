package github.alecsio.mmceaddons.common.hatch.abyssalcraft;

import github.alecsio.mmceaddons.common.hatch.AbstractMultiComponentRequirement;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentPotentialEnergy;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

import javax.annotation.Nonnull;

public class RequirementPotentialEnergy extends AbstractMultiComponentRequirement<PotentialEnergy, RequirementTypePotentialEnergy> {

    private final PotentialEnergy potentialEnergy;

    public RequirementPotentialEnergy(IOType actionType, PotentialEnergy potentialEnergy) {
        super((RequirementTypePotentialEnergy) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_POTENTIAL_ENERGY), actionType);
        this.potentialEnergy = potentialEnergy;
    }

    public static RequirementPotentialEnergy from(IOType ioType, float amount) {
        return new RequirementPotentialEnergy(ioType, new PotentialEnergy(amount));
    }

    public float getAmount() {
        return this.potentialEnergy.getEnergy();
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext recipeCraftingContext) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentPotentialEnergy &&
                cmp instanceof MachineComponentPotentialEnergyProvider &&
                cmp.ioType == getActionType();
    }

    @Override
    public ComponentRequirement<PotentialEnergy, RequirementTypePotentialEnergy> deepCopy() {
        return new RequirementPotentialEnergy(this.actionType, new PotentialEnergy(this.potentialEnergy.getEnergy()));
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.potential_energy";
    }

    @Override
    public JEIComponent<PotentialEnergy> provideJEIComponent() {
        return new JEIComponentPotentialEnergy(this.potentialEnergy);
    }
}
