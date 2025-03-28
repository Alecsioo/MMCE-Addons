package github.alecsio.mmceaddons.common.crafting.requirement.thaumicenergistics;

import github.alecsio.mmceaddons.common.crafting.component.ComponentEssentia;
import github.alecsio.mmceaddons.common.crafting.requirement.types.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.crafting.requirement.types.thaumicenergistics.RequirementTypeEssentia;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentEssentia;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentEssentiaProvider;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import thaumicenergistics.api.EssentiaStack;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementEssentia extends ComponentRequirement<EssentiaStack, RequirementTypeEssentia> {
    private final EssentiaStack essentia;

    public RequirementEssentia(EssentiaStack essentia, IOType type) {
        super((RequirementTypeEssentia) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_ESSENTIA), type);
        this.essentia = essentia;
    }

    public EssentiaStack getEssentiaStack() {
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
        return getEssentiaHandler(component).canHandle(this.essentia);
    }

    @Override
    public boolean startCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> getEssentiaHandler(component).handle(this.essentia));
        return true;
    }

    @Override
    public ComponentRequirement<EssentiaStack, RequirementTypeEssentia> deepCopy() {
        return new RequirementEssentia(new EssentiaStack(essentia.getAspect(), essentia.getAmount()), actionType);
    }

    @Override
    public ComponentRequirement<EssentiaStack, RequirementTypeEssentia> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "";
    }

    @Override
    public JEIComponent<EssentiaStack> provideJEIComponent() {
        return new JEIComponentEssentia(this);
    }

    private IRequirementHandler<EssentiaStack> getEssentiaHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<EssentiaStack>) component.getComponent().getContainerProvider();
    }
}
