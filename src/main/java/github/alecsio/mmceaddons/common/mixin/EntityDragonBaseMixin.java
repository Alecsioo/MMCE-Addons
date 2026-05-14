package github.alecsio.mmceaddons.common.mixin;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import github.alecsio.mmceaddons.common.hatch.iceandfire.IDragonBreathAcceptor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(value = EntityDragonBase.class, remap = false)
public abstract class EntityDragonBaseMixin {

    @Redirect(
            method = "updateBurnTarget",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 0
            )
    )
    private boolean MMCEA$allowDragonBreathAcceptor(Object obj, Class<?> originalClass) {
        return obj instanceof TileEntityDragonforgeInput
                || obj instanceof IDragonBreathAcceptor;
    }
}