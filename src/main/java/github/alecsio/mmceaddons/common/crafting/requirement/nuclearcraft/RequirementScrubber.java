package github.alecsio.mmceaddons.common.crafting.requirement.nuclearcraft;

import github.alecsio.mmceaddons.common.crafting.component.ComponentScrubber;
import github.alecsio.mmceaddons.common.crafting.requirement.IMultiChunkRequirement;
import github.alecsio.mmceaddons.common.crafting.requirement.types.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.crafting.requirement.types.nuclearcraft.RequirementTypeScrubber;
import github.alecsio.mmceaddons.common.crafting.requirement.validator.RequirementValidator;
import github.alecsio.mmceaddons.common.integration.jei.component.JEIComponentRadiation;
import github.alecsio.mmceaddons.common.integration.jei.ingredient.Radiation;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import github.alecsio.mmceaddons.common.tile.machinecomponent.MachineComponentScrubberProvider;
import github.alecsio.mmceaddons.common.tile.nuclearcraft.TileScrubberProvider;
import hellfirepvp.modularmachinery.common.crafting.helper.ComponentRequirement;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.crafting.helper.ProcessingComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.RecipeCraftingContext;
import hellfirepvp.modularmachinery.common.lib.RegistriesMM;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.modifier.RecipeModifier;
import hellfirepvp.modularmachinery.common.util.ResultChance;

import javax.annotation.Nonnull;
import java.util.List;

public class RequirementScrubber extends ComponentRequirement.PerTick<Radiation, RequirementTypeScrubber> implements IMultiChunkRequirement, IRequirementRadiation {

    private static final RequirementValidator requirementValidator = RequirementValidator.getInstance();

    private final int chunkRange;
    private final double amount;
    private final double minPerChunk;
    private final double maxPerChunk;

    public static RequirementScrubber from(int chunkRange) {
        requirementValidator.validateNotNegative(chunkRange, "Chunk range must be a positive number!");

        return new RequirementScrubber(IOType.INPUT, chunkRange, Double.MAX_VALUE, 0, Double.MAX_VALUE);
    }

    private RequirementScrubber(IOType actionType, int chunkRange, double amount, double minPerChunk, double maxPerChunk) {
        super((RequirementTypeScrubber) RegistriesMM.REQUIREMENT_TYPE_REGISTRY.getValue(ModularMachineryAddonsRequirements.KEY_REQUIREMENT_RADIATION_PER_TICK), actionType);
        this.chunkRange = chunkRange;
        this.amount = amount;
        this.minPerChunk = minPerChunk;
        this.maxPerChunk = maxPerChunk;
    }

    @Nonnull
    @Override
    public CraftCheck doIOTick(ProcessingComponent<?> processingComponent, RecipeCraftingContext recipeCraftingContext) {
        IRequirementHandler<RequirementScrubber> scrubberHandler = getRadiationHandler(processingComponent);

        scrubberHandler.handle(this);

        return CraftCheck.success();
    }

    @Nonnull
    @Override
    public CraftCheck finishCrafting(ProcessingComponent<?> component, RecipeCraftingContext context, ResultChance chance) {
        ((TileScrubberProvider) getRadiationHandler(component)).clearScrubbedChunks();
        return super.finishCrafting(component, context, chance);
    }

    @Override
    public boolean isValidComponent(ProcessingComponent<?> processingComponent, RecipeCraftingContext recipeCraftingContext) {
        MachineComponent<?> cmp = processingComponent.getComponent();
        return cmp.getComponentType() instanceof ComponentScrubber &&
                cmp instanceof MachineComponentScrubberProvider &&
                cmp.ioType == getActionType();
    }

    @Override
    public ComponentRequirement<Radiation, RequirementTypeScrubber> deepCopy() {
        return RequirementScrubber.from(chunkRange);
    }

    @Override
    public ComponentRequirement<Radiation, RequirementTypeScrubber> deepCopyModified(List<RecipeModifier> list) {
        return deepCopy();
    }

    @SuppressWarnings("unchecked")
    private IRequirementHandler<RequirementScrubber> getRadiationHandler(ProcessingComponent<?> component) {
        return (IRequirementHandler<RequirementScrubber> ) component.getComponent().getContainerProvider();
    }

    @Nonnull
    @Override
    public String getMissingComponentErrorMessage(IOType ioType) {
        return "error.modularmachineryaddons.component.invalid.scrubber";
    }

    @Override
    public JEIComponent<Radiation> provideJEIComponent() {
        return new JEIComponentRadiation(this, true);
    }

    @Override
    public IOType getIOType() {
        return actionType;
    }

    @Override
    public int getChunkRange() {
        return chunkRange;
    }

    @Override
    public double getAmount() {
        return amount;
    }

    @Override
    public IOType getType() {
        return actionType;
    }

    @Override
    public double getMinPerChunk() {
        return minPerChunk;
    }

    @Override
    public double getMaxPerChunk() {
        return maxPerChunk;
    }
}
