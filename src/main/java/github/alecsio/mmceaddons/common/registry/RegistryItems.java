package github.alecsio.mmceaddons.common.registry;

import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.item.ItemAdvancedConstructTool;
import github.alecsio.mmceaddons.common.item.ItemAdvancedMachineAssembler;
import github.alecsio.mmceaddons.common.item.ItemAdvancedMachineDisassembler;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;

// This class was adapted from a similar class in MMCE
public class RegistryItems {
    public static final List<Item> ITEM_BLOCKS = new ArrayList<>();
    protected static final List<Item> ITEM_MODEL_REGISTER = new ArrayList<>();

    public static ItemAdvancedConstructTool constructTool;
    public static ItemAdvancedMachineAssembler machineAssembler;
    public static ItemAdvancedMachineDisassembler machineDisassembler;

    public static void initialise() {

        constructTool = new ItemAdvancedConstructTool();
        machineAssembler = new ItemAdvancedMachineAssembler();
        machineDisassembler = new ItemAdvancedMachineDisassembler();

        constructTool.setRegistryName(ModularMachineryAddons.MODID, "advancedconstructtool").setTranslationKey(ModularMachineryAddons.MODID + "." + constructTool.getClass().getSimpleName().toLowerCase());
        machineAssembler.setRegistryName(ModularMachineryAddons.MODID, "advancedmachineassembler").setTranslationKey(ModularMachineryAddons.MODID + "." + machineAssembler.getClass().getSimpleName().toLowerCase());
        machineDisassembler.setRegistryName(ModularMachineryAddons.MODID, "advancedmachinedisassembler").setTranslationKey(ModularMachineryAddons.MODID + "." + machineDisassembler.getClass().getSimpleName().toLowerCase());

        ITEM_BLOCKS.add(constructTool);
        ITEM_BLOCKS.add(machineAssembler);
        ITEM_BLOCKS.add(machineDisassembler);

        registerItemBlocks();
        registerItemModels();
    }


    public void registerItemBlock(Item item) {
        ITEM_BLOCKS.add(item);
    }

    private static void registerItemBlocks() {
        ITEM_BLOCKS.forEach(RegistryItems::register);
    }

    private static <T extends Item> void register(T item) {
        ITEM_MODEL_REGISTER.add(item);
    }

    private static void registerItemModels() {
        ITEM_MODEL_REGISTER.forEach(ModularMachineryAddons.proxy::registerItemModel);
    }
}

