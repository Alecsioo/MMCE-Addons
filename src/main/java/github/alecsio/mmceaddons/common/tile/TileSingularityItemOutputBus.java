package github.alecsio.mmceaddons.common.tile;

import github.alecsio.mmceaddons.util.IOSingularityInventory;
import hellfirepvp.modularmachinery.common.block.prop.ItemBusSize;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileInventory;
import hellfirepvp.modularmachinery.common.util.IOInventory;

import javax.annotation.Nullable;

public class TileSingularityItemOutputBus extends TileSingularityItemBus implements MachineComponentTile {

    public TileSingularityItemOutputBus(ItemBusSize type) {
        super(type);
    }

    @Override
    public IOInventory buildInventory(TileInventory tile, int size) {

        int[] slots = new int[size];

        for (int i = 0; i < size; i++) {
            slots[i] = i;
        }

        return new IOSingularityInventory(tile, new int[0], slots, Integer.MAX_VALUE);
    }

    @Nullable
    public MachineComponent.ItemBus provideComponent() {
        return new MachineComponent.ItemBus(IOType.OUTPUT) {
            public IOInventory getContainerProvider() {
                return TileSingularityItemOutputBus.this.inventory;
            }
        };
    }
}
