package github.alecsio.mmceaddons.common.item.handler;

import github.alecsio.mmceaddons.common.item.BaseItemAdvancedMachineBuilder;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RightClickHandler {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        handleRightClick(event);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClick(PlayerInteractEvent.RightClickItem event) {
        handleRightClick(event);
    }

    private void handleRightClick(PlayerInteractEvent interactEvent) {
        Item item = interactEvent.getItemStack().getItem();
        World world = interactEvent.getWorld();
        BlockPos pos = interactEvent.getPos();

        if (item == Items.AIR || world.isRemote) {
            return;
        }

        boolean result = false;

        if (item instanceof BaseItemAdvancedMachineBuilder builder) {
            result = builder.onControllerRightClick(interactEvent.getEntityPlayer(), pos, world);
        }

        if (result) {
            interactEvent.setCanceled(true);
            interactEvent.setCancellationResult(EnumActionResult.SUCCESS);
        }
    }
}
