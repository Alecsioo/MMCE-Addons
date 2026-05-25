package github.alecsio.mmceaddons.common.registry;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.RequirementTypePotentialEnergy;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.meteor.RequirementTypeMeteor;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.will.RequirementTypeWillMultiChunk;
import github.alecsio.mmceaddons.common.hatch.iceandfire.RequirementTypeDragonBreath;
import github.alecsio.mmceaddons.common.hatch.mekanism.laser.RequirementTypeLaser;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.radiation.RequirementTypeRadiation;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.scrubber.RequirementTypeScrubber;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.flux.RequirementTypeFlux;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.vis.RequirementTypeVis;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ae2.essentia.RequirementTypeEssentia;
import github.alecsio.mmceaddons.common.hatch.vanilla.RequirementTypeBiome;
import github.alecsio.mmceaddons.common.hatch.vanilla.RequirementTypeDimension;
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
    public static final ResourceLocation KEY_REQUIREMENT_DRAGON_BREATH = new ResourceLocation(ModularMachineryAddons.MODID, "dragonBreath");
    public static final ResourceLocation KEY_REQUIREMENT_LASER = new ResourceLocation(ModularMachineryAddons.MODID, "laser");

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

        if (Mods.ICE_AND_FIRE.isPresent()) {
            registerRequirement(new RequirementTypeDragonBreath(), KEY_REQUIREMENT_DRAGON_BREATH);
        }

        if (Mods.MEKANISM.isPresent()) {
            registerRequirement(new RequirementTypeLaser(), KEY_REQUIREMENT_LASER);
        }
    }

    public static void registerRequirement(RequirementType<?, ?> requirement, ResourceLocation name) {
        requirement.setRegistryName(name);
        REQUIREMENTS.add(requirement);
    }
}
