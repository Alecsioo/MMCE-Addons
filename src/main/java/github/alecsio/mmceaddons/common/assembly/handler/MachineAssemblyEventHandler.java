package github.alecsio.mmceaddons.common.assembly.handler;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import github.alecsio.mmceaddons.ModularMachineryAddons;
import github.alecsio.mmceaddons.common.assembly.IMachineAssembly;
import github.alecsio.mmceaddons.common.assembly.MachineAssemblyManager;
import github.alecsio.mmceaddons.common.assembly.handler.exception.MultiblockBuilderNotFoundException;
import github.alecsio.mmceaddons.common.config.MMCEAConfig;
import github.alecsio.mmceaddons.common.network.MachineAssemblyMessage;
import hellfirepvp.modularmachinery.ModularMachinery;
import hellfirepvp.modularmachinery.common.tiles.base.TileMultiblockMachineController;
import ink.ikx.mmce.core.AssemblyConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MachineAssemblyEventHandler {

    public static final Cache<EntityPlayer, Boolean> ASSEMBLY_ACCESS_TOKEN = CacheBuilder.newBuilder()
            .expireAfterWrite(MMCEAConfig.cooldown, TimeUnit.SECONDS)
            .build();

    @SubscribeEvent()
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = player.world;
        if (event.phase == TickEvent.Phase.START || world.isRemote || world.getTotalWorldTime() % AssemblyConfig.tickBlock != 0) {
            return;
        }

        if (!(player instanceof EntityPlayerMP entityPlayerMP)) {
            return;
        }

        Collection<IMachineAssembly> machineAssemblies = MachineAssemblyManager.getMachineAssemblyListFromPlayer(player);
        if (machineAssemblies == null || machineAssemblies.isEmpty()) {
            return;
        }

        ModularMachinery.EXECUTE_MANAGER.addSyncTask(() -> {
            for (final IMachineAssembly assembly : machineAssemblies) {
                if (!(world.getTileEntity(assembly.getControllerPos()) instanceof TileMultiblockMachineController)) {
                    MachineAssemblyManager.removeMachineAssembly(assembly.getControllerPos());
                    player.sendMessage(new TextComponentTranslation(assembly.getErrorTranslationKey()));
                    return;
                }
                try {
                    assembly.assembleTick();
                } catch (MultiblockBuilderNotFoundException e) {
                    player.sendMessage(new TextComponentTranslation("error.assembler.not.found"));
                    MachineAssemblyManager.removeMachineAssembly(assembly.getControllerPos());
                }

                if (assembly.isCompleted()) {
                    MachineAssemblyManager.removeMachineAssembly(assembly.getControllerPos());

                    List<ItemStack> itemsToSend = new ArrayList<>();

                    List<ItemStack> unhandledBlocks = assembly.getUnhandledBlocks();
                    List<ItemStack> copy = new ArrayList<>(unhandledBlocks);
                    unhandledBlocks.clear();

                    while (!copy.isEmpty()) {
                        ItemStack first = copy.remove(0); // take first
                        Iterator<ItemStack> it = copy.iterator();
                        while (it.hasNext()) {
                            ItemStack other = it.next();
                            if (other.getItem() == first.getItem()) {
                                first.setCount(first.getCount() + other.getCount());
                                it.remove();
                            }
                        }
                        itemsToSend.add(first);
                    }

                    List<FluidStack> fluidsToSend = new ArrayList<>();

                    List<FluidStack> unhandledFluids = assembly.getUnhandledFluids();
                    List<FluidStack> fluidCopy = new ArrayList<>(unhandledFluids);
                    unhandledFluids.clear();

                    while (!fluidCopy.isEmpty()) {
                        FluidStack fluid = fluidCopy.remove(0);
                        Iterator<FluidStack> it = fluidCopy.iterator();
                        while (it.hasNext()) {
                            FluidStack other = it.next();
                            if (other.getFluid() == fluid.getFluid()) {
                                fluid = new FluidStack(fluid.getFluid(), fluid.amount + other.amount);
                                it.remove();
                            }
                        }
                        fluidsToSend.add(fluid);
                    }

                    MachineAssemblyMessage machineAssemblyMessage = new MachineAssemblyMessage(assembly.getCompletedTranslationKey(), assembly.getMissingBlocksTranslationKey(), itemsToSend, fluidsToSend);
                    ModularMachineryAddons.INSTANCE.sendTo(machineAssemblyMessage, entityPlayerMP);
                }
            }
        });
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        MachineAssemblyManager.removeMachineAssembly(event.player);
    }

}
