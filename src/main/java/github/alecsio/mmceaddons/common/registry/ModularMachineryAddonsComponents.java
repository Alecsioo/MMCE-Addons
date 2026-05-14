package github.alecsio.mmceaddons.common.registry;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.Mods;
import github.alecsio.mmceaddons.common.hatch.abyssalcraft.ComponentPotentialEnergy;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.ComponentMeteor;
import github.alecsio.mmceaddons.common.hatch.bloodmagic.ComponentWillMultiChunk;
import github.alecsio.mmceaddons.common.hatch.iceandfire.ComponentDragonBreath;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.ComponentRadiation;
import github.alecsio.mmceaddons.common.hatch.nuclearcraft.ComponentScrubber;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ComponentEssentia;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ComponentFlux;
import github.alecsio.mmceaddons.common.hatch.thaumcraft.ComponentVis;
import github.alecsio.mmceaddons.common.hatch.vanilla.ComponentBiome;
import github.alecsio.mmceaddons.common.hatch.vanilla.ComponentDimension;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class ModularMachineryAddonsComponents {

    public static IForgeRegistry<ComponentType> COMPONENT_TYPE_REGISTRY;
    public static final ResourceLocation KEY_COMPONENT_RADIATION = new ResourceLocation(ModularMachineryAddons.MODID, "radiation");
    public static final ResourceLocation KEY_COMPONENT_SCRUBBER = new ResourceLocation(ModularMachineryAddons.MODID, "scrubber");
    public static final ResourceLocation KEY_COMPONENT_METEOR = new ResourceLocation(ModularMachineryAddons.MODID, "meteor");
    public static final ResourceLocation KEY_COMPONENT_ESSENTIA = new ResourceLocation(ModularMachineryAddons.MODID, "essentia");
    public static final ResourceLocation KEY_COMPONENT_WILL = new ResourceLocation(ModularMachineryAddons.MODID, "willMultiChunk");
    public static final ResourceLocation KEY_COMPONENT_FLUX = new ResourceLocation(ModularMachineryAddons.MODID, "flux");
    public static final ResourceLocation KEY_COMPONENT_VIS = new ResourceLocation(ModularMachineryAddons.MODID, "vis");
    public static final ResourceLocation KEY_COMPONENT_BIOME = new ResourceLocation(ModularMachineryAddons.MODID, "biome");
    public static final ResourceLocation KEY_COMPONENT_DIMENSION = new ResourceLocation(ModularMachineryAddons.MODID, "dimension");
    public static final ResourceLocation KEY_COMPONENT_POTENTIAL_ENERGY = new ResourceLocation(ModularMachineryAddons.MODID, "potentialEnergy");
    public static final ResourceLocation KEY_COMPONENT_DRAGON_BREATH = new ResourceLocation(ModularMachineryAddons.MODID, "dragonBreath");

    public static final ArrayList<ComponentType> COMPONENTS = new ArrayList<>();

    public static void initComponents(RegistryEvent.Register<ComponentType> event) {
        COMPONENT_TYPE_REGISTRY = event.getRegistry();

        registerComponent(new ComponentBiome(), KEY_COMPONENT_BIOME);
        registerComponent(new ComponentDimension(), KEY_COMPONENT_DIMENSION);

        if (Mods.NUCLEARCRAFT.isPresent()) {
            registerComponent(new ComponentRadiation(), KEY_COMPONENT_RADIATION);
            registerComponent(new ComponentScrubber(), KEY_COMPONENT_SCRUBBER);
        }

        if (Mods.BLOODMAGIC.isPresent()) {
            registerComponent(new ComponentWillMultiChunk(), KEY_COMPONENT_WILL);
            registerComponent(new ComponentMeteor(), KEY_COMPONENT_METEOR);
        }

        if (Mods.THAUMICENERGISTICS.isPresent()) {
            registerComponent(new ComponentEssentia(), KEY_COMPONENT_ESSENTIA);
        }

        if (Mods.THAUMCRAFT.isPresent()) {
            registerComponent(new ComponentFlux(), KEY_COMPONENT_FLUX);
            registerComponent(new ComponentVis(), KEY_COMPONENT_VIS);
        }

        if (Mods.ABYSSALCRAFT.isPresent()) {
            registerComponent(new ComponentPotentialEnergy(), KEY_COMPONENT_POTENTIAL_ENERGY);
        }

        if (Mods.ICE_AND_FIRE.isPresent()) {
            registerComponent(new ComponentDragonBreath(), KEY_COMPONENT_DRAGON_BREATH);
        }
    }

    public static void registerComponent(ComponentType component, ResourceLocation name) {
        component.setRegistryName(name);
        if (COMPONENTS.contains(component)) {
            ModularMachineryAddons.logger.error("Component " + name + " already registered! This is probably a skill issue.");
            return;
        }
        COMPONENTS.add(component);
    }
}
