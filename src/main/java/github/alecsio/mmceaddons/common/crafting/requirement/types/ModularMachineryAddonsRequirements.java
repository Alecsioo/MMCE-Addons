package github.alecsio.mmceaddons.common.crafting.requirement.types;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.requirement.types.abyssalcraft.RequirementTypePotentialEnergy;
import github.alecsio.mmceaddons.common.crafting.requirement.types.bloodmagic.RequirementTypeMeteor;
import github.alecsio.mmceaddons.common.crafting.requirement.types.bloodmagic.RequirementTypeWillMultiChunk;
import github.alecsio.mmceaddons.common.crafting.requirement.types.nuclearcraft.RequirementTypeRadiation;
import github.alecsio.mmceaddons.common.crafting.requirement.types.nuclearcraft.RequirementTypeScrubber;
import github.alecsio.mmceaddons.common.crafting.requirement.types.thaumcraft.RequirementTypeFlux;
import github.alecsio.mmceaddons.common.crafting.requirement.types.thaumcraft.RequirementTypeVis;
import github.alecsio.mmceaddons.common.crafting.requirement.types.thaumicenergistics.RequirementTypeEssentia;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;

public class ModularMachineryAddonsRequirements {
    public static final ResourceLocation KEY_REQUIREMENT_RADIATION = new ResourceLocation(ModularMachineryAddons.MODID, "radiation");
    public static final ResourceLocation KEY_REQUIREMENT_RADIATION_PER_TICK = new ResourceLocation(ModularMachineryAddons.MODID, "scrubber");
    public static final ResourceLocation KEY_REQUIREMENT_METEOR = new ResourceLocation(ModularMachineryAddons.MODID, "meteor");
    public static final ResourceLocation KEY_REQUIREMENT_WILL_MULTI_CHUNK = new ResourceLocation(ModularMachineryAddons.MODID, "willMultiChunk");
    public static final ResourceLocation KEY_REQUIREMENT_ESSENTIA = new ResourceLocation(ModularMachineryAddons.MODID, "essentia");
    public static final ResourceLocation KEY_REQUIREMENT_FLUX = new ResourceLocation(ModularMachineryAddons.MODID, "flux");
    public static final ResourceLocation KEY_REQUIREMENT_VIS = new ResourceLocation(ModularMachineryAddons.MODID, "vis");
    public static final ResourceLocation KEY_REQUIREMENT_BIOME = new ResourceLocation(ModularMachineryAddons.MODID, "biome");
    public static final ResourceLocation KEY_REQUIREMENT_DIMENSION = new ResourceLocation(ModularMachineryAddons.MODID, "dimension");
    public static final ResourceLocation KEY_REQUIREMENT_POTENTIAL_ENERGY = new ResourceLocation(ModularMachineryAddons.MODID, "potentialEnergy");

    public static final ArrayList<RequirementType<?, ?>> REQUIREMENTS = new ArrayList<>();

    public static void initRequirements() {
        ModularMachineryAddons.logger.info("Initializing MMCE addons requirements...");
        REQUIREMENTS.clear();

        registerRequirement(new RequirementTypeBiome(), KEY_REQUIREMENT_BIOME);
        registerRequirement(new RequirementTypeDimension(), KEY_REQUIREMENT_DIMENSION);

        if (Mods.NUCLEARCRAFT.isPresent()) {
            registerRequirement(new RequirementTypeRadiation(), KEY_REQUIREMENT_RADIATION);
            registerRequirement(new RequirementTypeScrubber(), KEY_REQUIREMENT_RADIATION_PER_TICK);
        }

        if (Mods.BLOODMAGIC.isPresent()) {
            registerRequirement(new RequirementTypeWillMultiChunk(), KEY_REQUIREMENT_WILL_MULTI_CHUNK);
            registerRequirement(new RequirementTypeMeteor(), KEY_REQUIREMENT_METEOR);
        }

        if (Mods.THAUMICENERGISTICS.isPresent()) {
            registerRequirement(new RequirementTypeEssentia(), KEY_REQUIREMENT_ESSENTIA);
        }

        if (Mods.THAUMCRAFT.isPresent()) {
            registerRequirement(new RequirementTypeFlux(), KEY_REQUIREMENT_FLUX);
            registerRequirement(new RequirementTypeVis(), KEY_REQUIREMENT_VIS);
        }

        if (Mods.ABYSSALCRAFT.isPresent()) {
            registerRequirement(new RequirementTypePotentialEnergy(), KEY_REQUIREMENT_POTENTIAL_ENERGY);
        }
    }

    public static void registerRequirement(RequirementType<?, ?> requirement, ResourceLocation name) {
        requirement.setRegistryName(name);
        REQUIREMENTS.add(requirement);
    }
}
