package github.alecsio.mmceaddons.common.hatch.abyssalcraft;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentPotentialEnergy;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.PotentialEnergy;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementPotentialEnergy extends ComponentRequirement<PotentialEnergy, RequirementTypePotentialEnergy> {

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

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> processingComponent, RecipeCraftingContext recipeCraftingContext, List<ComponentOutputRestrictor> list) {
        return getPotentialEnergyHandler(processingComponent).canHandle(this);
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.INPUT) {
            getPotentialEnergyHandler(component).handle(this);
        }
        return true;
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.OUTPUT) {
            getPotentialEnergyHandler(component).handle(this);
        }
        return CraftCheck.success();
    }

    @Override
    public ComponentRequirement<PotentialEnergy, RequirementTypePotentialEnergy> deepCopy() {
        return new RequirementPotentialEnergy(this.actionType, new PotentialEnergy(this.potentialEnergy.getEnergy()));
    }

    @Override
    public ComponentRequirement<PotentialEnergy, RequirementTypePotentialEnergy> deepCopyModified(List<RecipeModifier> list) {
        return deepCopy();
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

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementPotentialEnergy> getPotentialEnergyHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementPotentialEnergy>) component.getComponent().getContainerProvider();
    }
}
