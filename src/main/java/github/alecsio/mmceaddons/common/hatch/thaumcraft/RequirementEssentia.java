package github.alecsio.mmceaddons.common.hatch.thaumcraft;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentEssentia;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Essentia;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import thaumcraft.api.aspects.Aspect;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementEssentia extends ComponentRequirement<Essentia, RequirementTypeEssentia> {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final Essentia essentia;

    public static RequirementEssentia from(IOType ioType, String aspect, int amount) {
        requirementValidator.validateNotNegative(amount, "Amount must be a positive number!");
        Aspect essentia = Aspect.getAspect(aspect);
        requirementValidator.validateNotNull(essentia, "Unknown aspect " + aspect);

        return new RequirementEssentia(new Essentia(essentia, amount), ioType);
    }

    public RequirementEssentia(Essentia essentia, IOType type) {
        super((RequirementTypeEssentia) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_ESSENTIA), type);
        this.essentia = essentia;
    }

    public Essentia getEssentiaStack() {
        return essentia;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentEssentia &&
                        cmp instanceof MachineComponentEssentiaProvider &&
                        cmp.ioType == getActionType();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> restrictions) {
        return getEssentiaHandler(component).canHandle(this);
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.INPUT) {
            getEssentiaHandler(component).handle(this);
        }
        return true;
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        if (getActionType() == IOType.OUTPUT) {
            getEssentiaHandler(component).handle(this);
        }
        return CraftCheck.success();
    }

    @Override
    public ComponentRequirement<Essentia, RequirementTypeEssentia> deepCopy() {
        return new RequirementEssentia(new Essentia(essentia.getAspect(), essentia.getAmount()), actionType);
    }

    @Override
    public ComponentRequirement<Essentia, RequirementTypeEssentia> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.essentia";
    }

    @Override
    public JEIComponent<Essentia> provideJEIComponent() {
        return new JEIComponentEssentia(this);
    }

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementEssentia> getEssentiaHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementEssentia>) component.getComponent().getContainerProvider();
    }
}
