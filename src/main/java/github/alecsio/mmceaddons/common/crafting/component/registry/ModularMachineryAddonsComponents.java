package github.alecsio.mmceaddons.common.crafting.component.registry;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.base.Mods;
import github.alecsio.mmceaddons.common.crafting.component.*;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;

public class ModularMachineryAddonsComponents {

    public static IForgeRegistry<ComponentType> COMPONENT_TYPE_REGISTRY;
    public static final ResourceLocation KEY_COMPONENT_RADIATION = new ResourceLocation(ModularMachineryAddons.MODID, "radiation");
    public static final ResourceLocation KEY_COMPONENT_METEOR = new ResourceLocation(ModularMachineryAddons.MODID, "meteor");
    public static final ResourceLocation KEY_COMPONENT_ESSENTIA = new ResourceLocation(ModularMachineryAddons.MODID, "essentia");
    public static final ResourceLocation KEY_COMPONENT_WILL = new ResourceLocation(ModularMachineryAddons.MODID, "willMultiChunk");
    public static final ResourceLocation KEY_COMPONENT_FLUX = new ResourceLocation(ModularMachineryAddons.MODID, "flux");
    public static final ResourceLocation KEY_COMPONENT_VIS = new ResourceLocation(ModularMachineryAddons.MODID, "vis");

    public static final ArrayList<ComponentType> COMPONENTS = new ArrayList<>();

    public static void initComponents(RegistryEvent.Register<ComponentType> event) {
        COMPONENT_TYPE_REGISTRY = event.getRegistry();
        if (Mods.NUCLEARCRAFT.isPresent()) {
            registerComponent(new ComponentRadiation(), KEY_COMPONENT_RADIATION);
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
