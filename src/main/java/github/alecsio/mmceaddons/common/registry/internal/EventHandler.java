package github.alecsio.mmceaddons.common.registry.internal;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsComponents;
import github.alecsio.mmceaddons.common.registry.ModularMachineryAddonsRequirements;
import github.alecsio.mmceaddons.common.registry.RegistryBlocks;
import github.alecsio.mmceaddons.common.registry.RegistryItems;
import hellfirepvp.modularmachinery.common.crafting.ComponentType;
import hellfirepvp.modularmachinery.common.crafting.requirement.type.RequirementType;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

// This class was adapted from a similar class in MMCE
public class EventHandler {

    @SubscribeEvent
    public void onComponentTypeRegister(RegistryEvent.Register<ComponentType> event) {
        ModularMachineryAddonsComponents.initComponents(event);
        for (ComponentType component : ModularMachineryAddonsComponents.COMPONENTS) {
            event.getRegistry().register(component);
        }
    }

    @SubscribeEvent
    public void onRequirementTypeRegister(RegistryEvent.Register event) {
        if (event.getGenericType() != RequirementType.class) {
            return;
        }
        ModularMachineryAddons.logger.info("Registering custom requirements...");

        ModularMachineryAddonsRequirements.initRequirements();

        for (RequirementType<?, ?> requirement : ModularMachineryAddonsRequirements.REQUIREMENTS) {
            ResourceLocation name = requirement.getRegistryName();
            ModularMachineryAddons.logger.info("Registering: " + name);
            event.getRegistry().register(requirement);
        }
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> event) {
        informAboutRegistrationFor(event);

        RegistryItems.initialise();

        event.getRegistry().registerAll(RegistryItems.ITEM_BLOCKS.toArray(new Item[0]));
    }

    @SubscribeEvent
    public void onBlockRegister(RegistryEvent.Register<Block> event) {
        informAboutRegistrationFor(event);

        RegistryBlocks.initialise();

        event.getRegistry().registerAll(RegistryBlocks.BLOCKS.toArray(new Block[0]));
    }

    private static void informAboutRegistrationFor(RegistryEvent.Register<?> event) {
        String registryName = event.getRegistry().getRegistrySuperType().getSimpleName();

        ModularMachineryAddons.logger.info("Registering " + registryName + "...");
    }
}
