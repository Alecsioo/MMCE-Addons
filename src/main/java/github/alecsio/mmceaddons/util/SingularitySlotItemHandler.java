package github.alecsio.mmceaddons.util;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SingularitySlotItemHandler extends SlotItemHandler {
    private final int handlerSlot;
    public SingularitySlotItemHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.handlerSlot = index;
    }

    @Nonnull
    public ItemStack insertItem(@Nonnull ItemStack stack) {
        return this.getItemHandler().insertItem(this.handlerSlot, stack, false);
    }
    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return Integer.MAX_VALUE;
    }
}
