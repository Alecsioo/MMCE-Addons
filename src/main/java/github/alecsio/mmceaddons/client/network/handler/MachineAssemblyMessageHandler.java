package github.alecsio.mmceaddons.client.network.handler;

import github.alecsio.mmceaddons.common.network.MachineAssemblyMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MachineAssemblyMessageHandler implements IMessageHandler<MachineAssemblyMessage, IMessage> {

    @Override
    public IMessage onMessage(MachineAssemblyMessage message, MessageContext ctx) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;

        player.sendMessage(new TextComponentTranslation(message.getCompletedTranslationKey()));

        List<ItemStack> unhandledBlocks = message.getUnhandledBlocks();
        List<FluidStack> unhandledFluids = message.getUnhandledFluids();

        if (unhandledBlocks.isEmpty() && unhandledFluids.isEmpty()) {
            return null;
        }

        List<Tuple<String, Long>> combined = Stream.concat(
                unhandledBlocks.stream()
                        .map(stack -> new Tuple<>(stack.getDisplayName(), (long) stack.getCount())),
                unhandledFluids.stream()
                        .map(fluid -> new Tuple<>(fluid.getLocalizedName(), (long) fluid.amount))
        ).collect(Collectors.toList());

        StringBuilder unhandledBlocksText = new StringBuilder().append("\n");
        for (Tuple<String, Long> tuple : combined) {
            unhandledBlocksText.append(tuple.getSecond()).append("x ").append(tuple.getFirst()).append("\n");
        }

        player.sendMessage(new TextComponentTranslation(message.getMissingBlocksTranslationKey(), unhandledBlocksText.toString()));

        return null;
    }
}
