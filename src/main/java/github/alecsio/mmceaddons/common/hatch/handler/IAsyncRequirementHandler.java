package github.alecsio.mmceaddons.common.hatch.handler;

import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import net.minecraftforge.fml.common.FMLCommonHandler;

public interface IAsyncRequirementHandler<T> extends IRequirementHandler<T> {

    @Override
    default CraftCheck canHandle(T requirement) {
        boolean isMainThread = FMLCommonHandler.instance().getMinecraftServerInstance().isCallingFromMinecraftThread();
        return isMainThread ? canHandleSync(requirement) : canHandleAsync(requirement);
    }

    /**
     * Performs the requirement check on the main Minecraft/server thread.
     *
     * <p>Any interaction with non-thread-safe classes or live game state should
     * happen here, such as world access, entities, tile entities, inventories,
     * capabilities, or other mutable Minecraft/Forge objects.
     */
    CraftCheck canHandleSync(T requirement);

    /**
     * Performs the requirement check off-thread.
     *
     * <p>This method should only work on local, cached, immutable, or otherwise
     * thread-safe state, such as snapshots prepared on the main thread. It must
     * not directly access non-thread-safe Minecraft/Forge classes or live game state.
     */
    CraftCheck canHandleAsync(T requirement);
}
