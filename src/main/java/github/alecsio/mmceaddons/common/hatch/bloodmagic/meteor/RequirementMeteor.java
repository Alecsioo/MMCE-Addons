package github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor;

import WayofTime.bloodmagic.meteor.MeteorRegistry;
import com.google.common.collect.Lists;
import github.alecsio.mmceaddons.common.hatch.handler.IAsyncRequirementHandler;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentMeteor;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Meteor;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class RequirementMeteor extends ComponentRequirement.MultiComponentRequirement<Meteor, RequirementTypeMeteor> {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final Meteor meteor;

    public static RequirementMeteor from(String catalystItem) {
        Item item = Item.getByNameOrId(catalystItem);
        requirementValidator.validateNotNull(item, String.format("Failed to load recipe for %s. Item %s doesn't exist!", RequirementMeteor.class, catalystItem));

        var catalystStack = new ItemStack(item);
        var meteor = MeteorRegistry.getMeteorForItem(catalystStack);
        requirementValidator.validateNotNull(meteor, String.format("Failed to find meteor with catalyst: %s", item.getRegistryName()));

        return new RequirementMeteor(IOType.OUTPUT, new Meteor(catalystStack, meteor.getComponents(), meteor.getExplosionStrength(), meteor.getRadius()));
    }

    private RequirementMeteor(IOType actionType, Meteor meteor) {
        super((RequirementTypeMeteor) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_METEOR), actionType);
        this.meteor = meteor;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cpn = component.getComponent();
        return cpn instanceof MachineComponentMeteorProvider &&
                cpn.getComponentType() instanceof ComponentMeteor &&
                cpn.ioType == getActionType();
    }

    @Override
    public ComponentRequirement<Meteor, RequirementTypeMeteor> deepCopy() {
        // I know, the components, but whatever
        return new RequirementMeteor(actionType, new Meteor(meteor.getCatalystStack(), meteor.getComponents(), meteor.getExplosionStrength(), meteor.getRadius()));
    }

    @Override
    public ComponentRequirement<Meteor, RequirementTypeMeteor> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.meteor";
    }

    @Override
    public JEIComponent<Meteor> provideJEIComponent() {
        return new JEIComponentMeteor(this.meteor);
    }

    @Nonnull
    @Override
    @SuppressWarnings("unchecked")
    public List<ProcessingComponent<?>> copyComponents(List<ProcessingComponent<?>> toCopy) {
        List<ProcessingComponent<?>> copy = new ArrayList<>(toCopy.size());

        toCopy.forEach(component -> {
            ProcessingComponent<?> cmp = new ProcessingComponent<>((MachineComponent<Object>) component.getComponent(), component.getProvidedComponent(), component.getTag());
            copy.add(cmp);
        });

        return copy;
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context) {
        List<IAsyncRequirementHandler<RequirementMeteor>> handlers = getMeteorHandlers(components);

        CraftCheck check = CraftCheck.failure("error.modularmachineryaddons.requirement.missing.meteor.missing");
        for (IAsyncRequirementHandler<RequirementMeteor> handler : handlers) {
            check = handler.canHandle(this);

            if (check.isSuccess()) {
                return check;
            }
        }

        return check;
    }

    @Override
    public void finishCrafting(List<ProcessingComponent<?>> components, RecipeCraftingContext context, ResultChance chance) {
        List<IAsyncRequirementHandler<RequirementMeteor>> handlers = getMeteorHandlers(components);

        for (IAsyncRequirementHandler<RequirementMeteor> handler : handlers) {
            if (handler.canHandle(this).isSuccess()) {
                handler.handle(this);
                return;
            }
        }
    }

    public Meteor getMeteor() {
        return meteor;
    }

    @SuppressWarnings("unchecked")
    private List<IAsyncRequirementHandler<RequirementMeteor>> getMeteorHandlers(List<ProcessingComponent<?>> components) {
        return Lists.transform(components, component -> component != null ? (IAsyncRequirementHandler<RequirementMeteor>) component.getProvidedComponent() : null);
    }
}
