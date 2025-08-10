package github.alecsio.mmceaddons.common.assembly;

import github.alecsio.mmceaddons.common.LoadedModsCache;
import ink.ikx.mmce.common.utils.StructureIngredient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMachineAssembly implements IMachineAssembly {

    public static final String MODE_INDEX = "modeIndex";
    public static final String AE2_ENCRYPTION_KEY = "encryptionKey";
    // For some reason in some prod envs ItemStack.EMPTY is modified
    protected static final ItemStack EMPTY = new ItemStack((Item) null);

    protected final World world;
    protected final BlockPos controllerPos;
    protected final EntityPlayer player;
    protected StructureIngredient ingredient;
    protected List<ItemStack> unhandledBlocks = new ArrayList<>();
    protected List<FluidStack> unhandledFluids = new ArrayList<>();
    // The last error that was reported by any of the handlers. If multiple errors were reported, only the last
    // one will be displayed to the player.
    protected TextComponentTranslation lastError;
    // Keeps track of whether any errors were present in the assembly. It is used to determine the message that should be
    // displayed at the end of the assembly
    protected boolean hadError = false;

    protected boolean completed = false;

    public AbstractMachineAssembly(World world, BlockPos controllerPos, EntityPlayer player, StructureIngredient ingredient) {
        this.world = world;
        this.controllerPos = controllerPos;
        this.player = player;
        this.ingredient = ingredient;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @Override
    public BlockPos getControllerPos() {
        return controllerPos;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    @Override
    public List<ItemStack> getUnhandledBlocks() {
        return unhandledBlocks;
    }

    @Override
    public List<FluidStack> getUnhandledFluids() {
        return unhandledFluids;
    }

    @Optional.Method(modid = LoadedModsCache.AE2)
    protected java.util.Optional<Long> getOptionalEncryptionKeyFrom(ItemStack assembler) {
        java.util.Optional<String> optEncryptionKey = getEncryptionKey(assembler);
        if (!optEncryptionKey.isPresent() || optEncryptionKey.get().isEmpty()) {
            lastError = new TextComponentTranslation("error.encryption.key.not.found");
            return java.util.Optional.empty();
        }
        return java.util.Optional.of(Long.parseLong(optEncryptionKey.get()));
    }

    protected void sendAndResetError() {
        if (lastError != null) {
            player.sendMessage(lastError);
            hadError = true;
            lastError = null;
        }
    }

    protected AssemblyModes getAssemblyModesFrom(ItemStack multiblockBuilder) {
        NBTTagCompound tag = multiblockBuilder.getTagCompound();
        int modeIndex;
        if (tag == null) {
            multiblockBuilder.setTagCompound(tag = new NBTTagCompound());
        }
        modeIndex = tag.getInteger(MODE_INDEX);
        return AssemblyModes.getSupportedModes().get(modeIndex);
    }

    protected abstract void handleCreativeAssembly();

    private java.util.Optional<String> getEncryptionKey(ItemStack stack) {
        NBTTagCompound tagCompound = stack.getTagCompound();
        String key = null;
        if (tagCompound != null) {
            key = tagCompound.getString(AE2_ENCRYPTION_KEY);
        }
        return java.util.Optional.ofNullable(key);
    }

}
