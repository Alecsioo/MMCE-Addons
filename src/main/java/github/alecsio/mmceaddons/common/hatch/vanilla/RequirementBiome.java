package github.alecsio.mmceaddons.common.hatch.vanilla;

import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.hatch.RequirementValidator;
import github.alecsio.mmceaddons.common.exception.RequirementPrerequisiteFailedException;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentBiome;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Biome;
import github.alecsio.mmceaddons.common.hatch.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.*;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementBiome extends ComponentRequirement<Biome, RequirementTypeBiome> {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final Biome biome;

    public static RequirementBiome from(IOType ioType, String biomeRegistryName) {
        if (ioType.equals(IOType.OUTPUT)) {throw new RequirementPrerequisiteFailedException("Input IOType is not supported");}

        requirementValidator.validateNotNull(biomeRegistryName, "Biome registry name cannot be null");

        return new RequirementBiome(ioType, new Biome(biomeRegistryName));
    }

    private RequirementBiome(IOType actionType, Biome biome) {
        super((RequirementTypeBiome) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_BIOME), actionType);
        this.biome = biome;
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> component, RecipeCraftingContext ctx) {
        MachineComponent<?> cmp = component.getComponent();
        return cmp.getComponentType() instanceof ComponentBiome &&
                cmp instanceof MachineComponentBiomeProvider &&
                cmp.ioType == getActionType();
    }

    @Nonnull
    @Override
    public CraftCheck canStartCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, List<ComponentOutputRestrictor> restrictions) {
        return getBiomeHandler(component).canHandle(this);
    }

    @Override
    public ComponentRequirement<Biome, RequirementTypeBiome> deepCopy() {
        return new RequirementBiome(getActionType(), new Biome(this.biome.getRegistryName(), this.biome.getName()));
    }

    @Override
    public ComponentRequirement<Biome, RequirementTypeBiome> deepCopyModified(List<RecipeModifier> modifiers) {
        return deepCopy();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.biome";
    }

    @Override
    public JEIComponent<Biome> provideJEIComponent() {
        return new JEIComponentBiome(this.biome);
    }

    public Biome getBiome() {
        return biome;
    }

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementBiome> getBiomeHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementBiome>) component.getComponent().getContainerProvider();
    }
}
