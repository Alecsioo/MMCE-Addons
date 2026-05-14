package github.alecsio.mmceaddons.common.mixin;

import github.alecsio.mmceaddons.common.MMCEAConfig;
import hellfirepvp.modularmachinery.common.selection.PlayerStructureSelectionHelper;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(PlayerStructureSelectionHelper.StructureSelection.class)
public class StructureSelectionMixin {

    @ModifyArg(method = "compressAsArray(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;)Lhellfirepvp/modularmachinery/common/util/BlockArray;",
    at = @At(value = "INVOKE", target = "Lhellfirepvp/modularmachinery/common/util/BlockArray$BlockInformation;setMatchingTag(Lnet/minecraft/nbt/NBTTagCompound;)V"), remap = false)
    private NBTTagCompound onCompressAsArray(NBTTagCompound matchingTag) {
        if (MMCEAConfig.constructNBT) {
            return matchingTag;
        }
        return null;
    }
}
