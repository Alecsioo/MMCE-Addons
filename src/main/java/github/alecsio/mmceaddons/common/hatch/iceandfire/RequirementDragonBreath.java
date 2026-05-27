package github.alecsio.mmceaddons.common.hatch.iceandfire;

import github.alecsio.mmceaddons.common.hatch.AbstractMultiComponentRequirement;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentDragonBreath;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;

import javax.annotation.Nonnull;

public class RequirementDragonBreath extends AbstractMultiComponentRequirement<DragonBreath, RequirementTypeDragonBreath> {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final DragonBreath dragonBreath;

    public RequirementDragonBreath(IOType actionType, DragonBreath dragonBreath) {
        super((RequirementTypeDragonBreath) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_DRAGON_BREATH), actionType);
        this.dragonBreath = dragonBreath;
    }

    public static RequirementDragonBreath from(IOType ioType, String type, int amount) {
        requirementValidator.validateNotNegative(amount, "Amount cannot be negative");

        DragonType dragonType;
        try {
            dragonType = DragonType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RequirementPrerequisiteFailedException("Invalid dragon type: " + type);
        }

        return new RequirementDragonBreath(ioType, new DragonBreath(dragonType, amount));
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext recipeCraftingContext) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentDragonBreath &&
                cmp instanceof MachineComponentDragonBreathProvider &&
                cmp.ioType == getActionType();
    }

    public int getAmount() {
        return dragonBreath.getAmount();
    }

    public DragonType getType() {
        return dragonBreath.getDragonType();
    }

    @Override
    public ComponentRequirement<DragonBreath, RequirementTypeDragonBreath> deepCopy() {
        return new RequirementDragonBreath(actionType, new DragonBreath(dragonBreath.getDragonType(), dragonBreath.getAmount()));
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.dragon";
    }

    @Override
    public JEIComponent<DragonBreath> provideJEIComponent() {
        return new JEIComponentDragonBreath(dragonBreath);
    }
}
