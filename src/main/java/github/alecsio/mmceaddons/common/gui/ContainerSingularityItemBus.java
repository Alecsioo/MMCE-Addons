package github.alecsio.mmceaddons.common.gui;

import github.alecsio.mmceaddons.util.SingularitySlotItemHandler;
import hellfirepvp.modularmachinery.common.container.ContainerItemBus;
import hellfirepvp.modularmachinery.common.tiles.base.TileItemBus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ContainerSingularityItemBus extends ContainerItemBus {

    public ContainerSingularityItemBus(TileItemBus owner, EntityPlayer opening) {
        super(owner, opening);
    }


    @Nonnull
    @Override
    public ItemStack slotClick(int slotId, int dragType, @Nonnull ClickType clickTypeIn, EntityPlayer player) {
        InventoryPlayer inventoryplayer = player.inventory;
        if (slotId > 0 && this.inventorySlots.get(slotId) instanceof SingularitySlotItemHandler singularitySlot && clickTypeIn == ClickType.PICKUP && (dragType == 0 || dragType == 1)) {
            ItemStack heldStack = inventoryplayer.getItemStack();
            ItemStack inventoryStack = singularitySlot.getStack();


            if (!heldStack.isEmpty() && !inventoryStack.isEmpty() && heldStack.getItem() == inventoryStack.getItem() && heldStack.getMetadata() == inventoryStack.getMetadata() && ItemStack.areItemStackTagsEqual(heldStack, inventoryStack)) {
                int amountToDrop = dragType == 0 ? heldStack.getCount() : 1;

                if (amountToDrop > singularitySlot.getItemStackLimit(heldStack) - inventoryStack.getCount()) {
                    amountToDrop = singularitySlot.getItemStackLimit(heldStack) - inventoryStack.getCount();
                }

                ItemStack toReturn = heldStack.copy();
                heldStack.shrink(amountToDrop);
                inventoryStack.grow(amountToDrop);
                return toReturn;
            }
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }
}
