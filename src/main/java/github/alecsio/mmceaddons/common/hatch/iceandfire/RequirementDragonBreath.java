package github.alecsio.mmceaddons.common.hatch.iceandfire;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentDragonBreath;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.DragonBreath;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementDragonBreath extends ComponentRequirement<DragonBreath, RequirementTypeDragonBreath> {

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

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> processingComponent, RecipeCraftingContext recipeCraftingContext, List<ComponentOutputRestrictor> list) {
        return getDragonBreathHandler(processingComponent).canHandle(this);
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.INPUT) {
            getDragonBreathHandler(component).handle(this);
        }
        return true;
    }

    @Override
    public ComponentRequirement<DragonBreath, RequirementTypeDragonBreath> deepCopy() {
        return new RequirementDragonBreath(actionType, new DragonBreath(dragonBreath.getDragonType(), dragonBreath.getAmount()));
    }

    @Override
    public ComponentRequirement<DragonBreath, RequirementTypeDragonBreath> deepCopyModified(List<RecipeModifier> list) {
        return deepCopy();
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

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementDragonBreath> getDragonBreathHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementDragonBreath>) component.getComponent().getContainerProvider();
    }
}
