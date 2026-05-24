package github.alecsio.mmceaddons.common.hatch.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import github.alecsio.mmceaddons.common.MMCEAConfig;
import github.alecsio.mmceaddons.common.hatch.AbstractSnapshotMachineComponent;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@ParametersAreNonnullByDefault
public class TileDragonBreathProvider extends AbstractSnapshotMachineComponent<RequirementDragonBreath> implements MachineComponentTile, IDragonBreathAcceptor {

    // Shared by main and recipe thread
    private final AtomicReference<DragonType> type = new AtomicReference<>(DragonType.EMPTY);
    private final AtomicInteger charges = new AtomicInteger(0);

    // Only used in main thread
    private boolean typeLocked;
    private final int breathingTicks = 8 * 20;
    private final Map<UUID, Integer> luredDragons = new HashMap<>();

    public TileDragonBreathProvider(DragonType type) {
        this.type.set(type);
    }

    @Override
    public void lureDragons() {
        List<EntityDragonBase> dragons = world.getEntitiesWithinAABB(EntityDragonBase.class, new AxisAlignedBB(pos.getX() - 50, pos.getY() - 50, pos.getZ() - 50, pos.getX() + 50, pos.getY() + 50, pos.getZ() + 50));

        final DragonType dragonType = this.type.get();

        for (EntityDragonBase dragon : dragons) {
            if (dragon.burningTarget != null && !dragon.burningTarget.equals(this.pos)) {
                continue;
            }

            if (isFull()) {
                resetBurningTarget(dragon);
                return;
            }

            com.github.alexthe666.iceandfire.entity.DragonType dType = dragon.dragonType;
            if (dragonType != null && dragonType != DragonType.EMPTY && !dType.getName().equalsIgnoreCase(dragonType.name())) {
                resetBurningTarget(dragon);
                continue;
            }

            UUID id = dragon.getUniqueID();
            int remaining = luredDragons.getOrDefault(id, breathingTicks);

            if (remaining > 0) {
                dragon.burningTarget = this.pos;
                luredDragons.put(id, remaining - 1);
            } else {
                resetBurningTarget(dragon);
            }
        }
    }

    private void resetBurningTarget(EntityDragonBase dragon) {
        if (this.pos.equals(dragon.burningTarget)) {
            dragon.burningTarget = null;
            dragon.setBreathingFire(false);
            luredDragons.remove(dragon.getUniqueID());
        }
    }

    @Override
    public void onHitWithFlame(DragonType dragonType) {
        DragonType localType = this.type.get();
        if (isFull() || (localType != null && localType != DragonType.EMPTY && dragonType != localType)) {
            updateSnapshot();
            return;
        }

        type.set(dragonType);
        charges.incrementAndGet();
        refreshScheduler.recordSuccess();
        if (!world.isRemote) {
            world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockDragonBreathInput.DRAGON_TYPE, dragonType));
            markNoUpdateSync();
        }
    }

    @Override
    public void invalidate() {
        luredDragons.clear();
        super.invalidate();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
    }

    // Empty stack => unlock
    // Dragon heart => lock to heart type
    public DragonType onRightClicked(ItemStack stack) {
        DragonType localType = this.type.get();
        if (localType == DragonType.EMPTY && (stack == null || stack.isEmpty())) return null;

        final int localCharges = charges.get();
        if (localType != DragonType.EMPTY && stack.isEmpty()) {
            typeLocked = false;
            if (localCharges <= 0) {
                type.set(DragonType.EMPTY);
                world.setBlockState(pos, world.getBlockState(this.pos).withProperty(BlockDragonBreathInput.DRAGON_TYPE, DragonType.EMPTY));
            }
            markNoUpdateSync();
            updateSnapshot();
            return null;
        }

        if (localType != DragonType.EMPTY && !stack.isEmpty() && localCharges > 0) {
            updateSnapshot();
            return localType;
        }

        Item item = stack.getItem();
        DragonType lockedType = localType;
        if (item == IafItemRegistry.fire_dragon_heart) {
            lockedType = DragonType.FIRE;
        } else if (item == IafItemRegistry.ice_dragon_heart) {
            lockedType = DragonType.ICE;
        } else if (item == IafItemRegistry.lightning_dragon_heart) {
            lockedType = DragonType.LIGHTNING;
        }

        type.set(lockedType);
        typeLocked = true;
        if (!world.isRemote) {
            world.setBlockState(pos, world.getBlockState(this.pos).withProperty(BlockDragonBreathInput.DRAGON_TYPE, lockedType));
            markNoUpdateSync();
        }
        updateSnapshot();
        return lockedType;
    }

    @Nullable
    @Override
    public MachineComponent<?> provideComponent() {
        return new MachineComponentDragonBreathProvider(IOType.INPUT, this);
    }

    @Override
    public void handle(RequirementDragonBreath requirement) {
        final int localCharges = charges.addAndGet(-requirement.getAmount());
        refreshScheduler.recordSuccess();

        if (!typeLocked && localCharges <= 0) {
            type.set(DragonType.EMPTY);
            world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockDragonBreathInput.DRAGON_TYPE, DragonType.EMPTY));
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        typeLocked = compound.getBoolean("typeLocked");
        charges.set(compound.getInteger("charges"));
        type.set(DragonType.valueOf(compound.getString("type")));
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setBoolean("typeLocked", typeLocked);
        compound.setInteger("charges", charges.get());
        compound.setString("type", type.get().toString());
    }

    private boolean isFull() {
        return charges.get() >= MMCEAConfig.dragonBreathChargesCapacity;
    }

    public DragonType getType() {
        return type.get();
    }

    public boolean isTypeLocked() {
        return typeLocked;
    }

    public int getCharges() {
        return charges.get();
    }

    @Override
    protected void updateSnapshot() {
        lureDragons();
    }

    @Override
    protected CraftCheck checkSnapshot(RequirementDragonBreath requirement) {
        final DragonType localType = type.get();
        boolean typeMatches = localType != DragonType.EMPTY && localType.equals(requirement.getType());
        boolean enoughCharges = this.charges.get() >= requirement.getAmount();

        if (!typeMatches) {
            return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.type");
        }

        if (!enoughCharges) {
            return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.charges");
        }

        return CraftCheck.success();
    }
}
