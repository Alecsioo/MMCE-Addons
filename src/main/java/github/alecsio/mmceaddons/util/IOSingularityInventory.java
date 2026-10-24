package github.alecsio.mmceaddons.util;

import hellfirepvp.modularmachinery.common.tiles.base.TileEntitySynchronized;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import hellfirepvp.modularmachinery.common.util.ItemUtils;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class IOSingularityInventory extends IOInventory {
    public IOSingularityInventory(TileEntitySynchronized owner, int[] inSlots, int[] outSlots, int maxSize) {
        super(owner, inSlots, outSlots);
        Arrays.fill(this.slotLimits, maxSize);
    }

    // The parent's canMergeItemStacks refuses unstackable items, which blocks automation from adding to an
    // occupied slot even though the GUI allows it. A singularity slot holds any number of identical items.
    private static boolean canMergeSingularityStacks(@Nonnull ItemStack stack, @Nonnull ItemStack other) {
        return !stack.isEmpty() && !other.isEmpty() && stack.isItemEqual(other) && ItemStack.areItemStackTagsEqual(stack, other);
    }

    protected ItemStack insertItemInternal(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!this.allowAnySlots && !arrayContains(this.inSlots, slot)) {
            return stack;
        } else {
            SlotStackHolder holder = this.inventory[slot];
            if (holder == null) {
                return stack;
            } else {
                ItemStack toInsert = ItemUtils.copyStackWithSize(stack, stack.getCount());
                if (!holder.itemStack.get().isEmpty()) {
                    ItemStack existing = ItemUtils.copyStackWithSize(holder.itemStack.get(), holder.itemStack.get().getCount());
                    int max = this.getSlotLimit(slot);
                    if (existing.getCount() < max && canMergeSingularityStacks(existing, toInsert)) {
                        int movable = Math.min(max - existing.getCount(), stack.getCount());
                        if (!simulate) {
                            holder.itemStack.get().grow(movable);
                        }

                        if (movable >= stack.getCount()) {
                            return ItemStack.EMPTY;
                        } else {
                            ItemStack copy = stack.copy();
                            copy.shrink(movable);
                            return copy;
                        }
                    } else {
                        return stack;
                    }
                } else {
                    int max = this.getSlotLimit(slot);
                    if (max >= stack.getCount()) {
                        if (!simulate) {
                            holder.itemStack.set(stack.copy());
                        }

                        return ItemStack.EMPTY;
                    } else {
                        ItemStack copy = stack.copy();
                        copy.setCount(max);
                        if (!simulate) {
                            holder.itemStack.set(copy);
                        }

                        copy = stack.copy();
                        copy.shrink(max);
                        return copy;
                    }
                }
            }
        }
    }
}
