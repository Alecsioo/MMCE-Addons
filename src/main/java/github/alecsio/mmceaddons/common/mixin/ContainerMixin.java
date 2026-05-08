package github.alecsio.mmceaddons.common.mixin;

import github.alecsio.mmceaddons.util.SingularitySlotItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = hellfirepvp.modularmachinery.common.container.ContainerItemBus.class)
public class ContainerMixin {

    @Redirect(method = "addInventorySlots(Lnet/minecraftforge/items/IItemHandlerModifiable;Lhellfirepvp/modularmachinery/common/block/prop/ItemBusSize;)V", at = @At(value = "NEW", target = "(Lnet/minecraftforge/items/IItemHandler;III)Lnet/minecraftforge/items/SlotItemHandler;"), remap=false)
    public SlotItemHandler replaceSlotItemHandlers(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        return new SingularitySlotItemHandler(itemHandler, index, xPosition, yPosition);
    }
}
