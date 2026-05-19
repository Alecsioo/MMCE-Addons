package github.alecsio.mmceaddons.common.mixin.iceandfire;

import com.github.alexthe666.iceandfire.entity.IafDragonDestructionManager;
import com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput;
import github.alecsio.mmceaddons.common.hatch.iceandfire.DragonType;
import github.alecsio.mmceaddons.common.hatch.iceandfire.IDragonBreathAcceptor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = IafDragonDestructionManager.class, remap = false)
public class IafDragonDestructionManagerMixin {

    @Redirect(
            method = "destroyAreaFire",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 0
            )
    )
    private static boolean MMCEA$onHitAcceptorFire0(Object obj, Class<?> originalClass) {
        if (obj instanceof IDragonBreathAcceptor acceptor) {
            acceptor.onHitWithFlame(DragonType.FIRE);
        }
        return obj instanceof TileEntityDragonforgeInput;
    }

    @Redirect(
            method = "destroyAreaFire",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 2
            )
    )
    private static boolean MMCEA$onHitAcceptorFire1(Object obj, Class<?> originalClass) {
        if (obj instanceof IDragonBreathAcceptor acceptor) {
            acceptor.onHitWithFlame(DragonType.FIRE);
        }
        return obj instanceof TileEntityDragonforgeInput;
    }

    @Redirect(
            method = "destroyAreaIce",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 0
            )
    )
    private static boolean MMCEA$onHitAcceptorIce0(Object obj, Class<?> originalClass) {
        if (obj instanceof IDragonBreathAcceptor acceptor) {
            acceptor.onHitWithFlame(DragonType.ICE);
        }
        return obj instanceof TileEntityDragonforgeInput;
    }

    @Redirect(
            method = "destroyAreaIce",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 2
            )
    )
    private static boolean MMCEA$onHitAcceptorIce1(Object obj, Class<?> originalClass) {
        if (obj instanceof IDragonBreathAcceptor acceptor) {
            acceptor.onHitWithFlame(DragonType.ICE);
        }
        return obj instanceof TileEntityDragonforgeInput;
    }

    @Redirect(
            method = "destroyAreaLightning",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 0
            )
    )
    private static boolean MMCEA$onHitAcceptorLightning0(Object obj, Class<?> originalClass) {
        if (obj instanceof IDragonBreathAcceptor acceptor) {
            acceptor.onHitWithFlame(DragonType.LIGHTNING);
        }
        return obj instanceof TileEntityDragonforgeInput;
    }

    @Redirect(
            method = "destroyAreaLightning",
            at = @At(
                    value = "CONSTANT",
                    args = "classValue=com.github.alexthe666.iceandfire.entity.tile.TileEntityDragonforgeInput",
                    ordinal = 2
            )
    )
    private static boolean MMCEA$onHitAcceptorLightning1(Object obj, Class<?> originalClass) {
        if (obj instanceof IDragonBreathAcceptor acceptor) {
            acceptor.onHitWithFlame(DragonType.LIGHTNING);
        }
        return obj instanceof TileEntityDragonforgeInput;
    }

}
