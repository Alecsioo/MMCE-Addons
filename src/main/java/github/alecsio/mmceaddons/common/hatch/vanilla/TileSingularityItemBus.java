package github.alecsio.mmceaddons.common.hatch.vanilla;

import hellfirepvp.modularmachinery.common.block.prop.ItemBusSize;
import hellfirepvp.modularmachinery.common.tiles.base.TileItemBus;
import hellfirepvp.modularmachinery.common.util.IOInventory;
import net.minecraft.nbt.NBTTagCompound;

abstract public class TileSingularityItemBus extends TileItemBus {

    public TileSingularityItemBus() {}

    public TileSingularityItemBus(ItemBusSize size) {
        super(size);
    }
    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        NBTTagCompound inventoryTag = compound.getCompoundTag("inventory");
        // super.readCustomNBT() restores the persisted backing inventory size,
        // but creates a plain IOInventory without singularity slot limits.
        int slotCount = this.inventory.getSlots();
        IOInventory restored = buildInventory(this, slotCount);
        restored.readNBT(inventoryTag);
        this.inventory = restored;
    }
}