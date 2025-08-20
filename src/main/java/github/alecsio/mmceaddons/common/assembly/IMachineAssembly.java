package github.alecsio.mmceaddons.common.assembly;

import github.alecsio.mmceaddons.common.assembly.handler.exception.MultiblockBuilderNotFoundException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

public interface IMachineAssembly {

    BlockPos getControllerPos();
    EntityPlayer getPlayer();
    void assembleTick() throws MultiblockBuilderNotFoundException;
    boolean isCompleted();
    String getCompletedTranslationKey();
    String getErrorTranslationKey();
    String getMissingBlocksTranslationKey();
    List<ItemStack> getUnhandledBlocks();
    List<FluidStack> getUnhandledFluids();
}
