package github.alecsio.mmceaddons.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

// Sent from server -> client
public class MachineAssemblyMessage implements IMessage {

    private static final String ITEM_LIST_KEY = "itemList";
    private static final String FLUID_LIST_KEY = "fluidList";

    private String completedTranslationKey;
    private String missingBlocksTranslationKey;
    private List<ItemStack> unhandledBlocks;
    private List<FluidStack> unhandledFluids;

    public MachineAssemblyMessage() {
    }

    public MachineAssemblyMessage(String completedTranslationKey, String missingBlocksTranslationKey, List<ItemStack> unhandledBlocks, List<FluidStack> unhandledFluids) {
        this.completedTranslationKey = completedTranslationKey;
        this.missingBlocksTranslationKey = missingBlocksTranslationKey;
        this.unhandledBlocks = unhandledBlocks;
        this.unhandledFluids = unhandledFluids;
    }

    public String getCompletedTranslationKey() {
        return completedTranslationKey;
    }

    public String getMissingBlocksTranslationKey() {
        return missingBlocksTranslationKey;
    }

    public List<ItemStack> getUnhandledBlocks() {
        return unhandledBlocks;
    }

    public List<FluidStack> getUnhandledFluids() {
        return unhandledFluids;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        completedTranslationKey = ByteBufUtils.readUTF8String(buf);
        missingBlocksTranslationKey = ByteBufUtils.readUTF8String(buf);
        this.unhandledBlocks = new ArrayList<>();
        this.unhandledFluids = new ArrayList<>();

        NBTTagCompound tagCompound = ByteBufUtils.readTag(buf);
        NBTTagList itemList = tagCompound.getTagList(ITEM_LIST_KEY, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < itemList.tagCount(); i++) {
            NBTTagCompound itemTagCompound = itemList.getCompoundTagAt(i);
            if (!itemList.isEmpty()) {
                ItemStack itemStack = new ItemStack(itemTagCompound);
                if (!itemStack.isEmpty()) {
                    this.unhandledBlocks.add(itemStack);
                }
            }
        }
        NBTTagList fluidList = tagCompound.getTagList(FLUID_LIST_KEY, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < fluidList.tagCount(); i++) {
            NBTTagCompound fluidTagCompound = fluidList.getCompoundTagAt(i);
            if (!fluidTagCompound.isEmpty()) {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluidTagCompound);
                if (fluidStack != null) {
                    this.unhandledFluids.add(fluidStack);
                }
            }
        }

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, completedTranslationKey);
        ByteBufUtils.writeUTF8String(buf, missingBlocksTranslationKey);

        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList itemList = new NBTTagList();
        for (ItemStack stack : unhandledBlocks) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            itemList.appendTag(stackTag);
        }
        tag.setTag(ITEM_LIST_KEY, itemList);

        NBTTagList fluidList = new NBTTagList();
        for (FluidStack stack : unhandledFluids) {
            NBTTagCompound stackTag = new NBTTagCompound();
            stack.writeToNBT(stackTag);
            fluidList.appendTag(stackTag);
        }
        tag.setTag(FLUID_LIST_KEY, fluidList);

        ByteBufUtils.writeTag(buf, tag);
    }
}
