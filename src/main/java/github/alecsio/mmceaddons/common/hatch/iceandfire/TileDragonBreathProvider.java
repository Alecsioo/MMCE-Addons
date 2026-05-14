package github.alecsio.mmceaddons.common.hatch.iceandfire;

import com.github.alexthe666.iceandfire.entity.EntityDragonBase;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import github.alecsio.mmceaddons.common.MMCEAConfig;
import github.alecsio.mmceaddons.common.tile.handler.IRequirementHandler;
import hellfirepvp.modularmachinery.common.crafting.helper.CraftCheck;
import hellfirepvp.modularmachinery.common.machine.IOType;
import hellfirepvp.modularmachinery.common.machine.MachineComponent;
import hellfirepvp.modularmachinery.common.tiles.base.MachineComponentTile;
import hellfirepvp.modularmachinery.common.tiles.base.TileColorableMachineComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

import javax.annotation.Nullable;

public class TileDragonBreathProvider extends TileColorableMachineComponent implements MachineComponentTile, IDragonBreathAcceptor, IRequirementHandler<RequirementDragonBreath>, ITickable {

    private DragonType type;
    private boolean typeLocked;
    private int charges;

    private int ticksSinceLastBreath = 0;
    private final int ticksPerBreath = 40;

    @Override
    public void lureDragons() {
        for (EntityDragonBase dragon : world.getEntitiesWithinAABB(EntityDragonBase.class, new AxisAlignedBB(pos.getX() - 50, pos.getY() - 50, pos.getZ() - 50, pos.getX() + 50, pos.getY() + 50, pos.getZ() + 50))) {
            ticksSinceLastBreath--;
            if (ticksSinceLastBreath <= 0) {
                ticksSinceLastBreath = ticksPerBreath;
                com.github.alexthe666.iceandfire.entity.DragonType dType = dragon.dragonType;
                if (dType == null || (type != null && !dType.getName().equalsIgnoreCase(type.name()))) return;
                if (isFull()) {
                    return;
                }

                if (dragon.canPositionBeSeen(this.pos.getX(), this.pos.getY(), this.pos.getZ())) {
                    dragon.burningTarget = this.pos;
                    onHitWithFlame(DragonType.valueOf(dType.getName().toUpperCase()));
                }
            }
        }
    }

    @Override
    public void onHitWithFlame(DragonType dragonType) {
        if (isFull() || (type != null && dragonType != type)) return;

        type = dragonType;
        ticksSinceLastBreath = ticksPerBreath;
        charges = charges + 1;
    }

    // Empty stack => unlock
    // Dragon heart => lock to heart type
    public DragonType onRightClicked(ItemStack stack) {
        if (type == null && (stack == null || stack.isEmpty())) return null;

        if (type != null && stack.isEmpty()) {
            typeLocked = false;
            return null;
        }

        // Can't lock to some other type while charges > 0
        if (type != null && !stack.isEmpty() && charges > 0) {
            return type;
        }

        Item item = stack.getItem();
        DragonType lockedType = this.type;
        if (item == IafItemRegistry.fire_dragon_heart) {
            lockedType = DragonType.FIRE;
        } else if (item == IafItemRegistry.ice_dragon_heart) {
            lockedType = DragonType.ICE;
        } else if (item == IafItemRegistry.lightning_dragon_heart) {
            lockedType = DragonType.LIGHTNING;
        }

        typeLocked = true;
        type = lockedType;

        return lockedType;
    }


    @Nullable
    @Override
    public MachineComponent<?> provideComponent() {
        return new MachineComponentDragonBreathProvider(IOType.INPUT, this);
    }

    @Override
    public CraftCheck canHandle(RequirementDragonBreath requirement) {
        boolean typeMatches = this.type != null && this.type.equals(requirement.getType());
        boolean enoughCharges = this.charges >= requirement.getAmount();

        if (!typeMatches) {
            return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.type");
        }

        if (!enoughCharges) {
            return CraftCheck.failure("error.modularmachineryaddons.requirement.missing.charges");
        }

        return CraftCheck.success();
    }

    @Override
    public void handle(RequirementDragonBreath requirement) {
        charges -= requirement.getAmount();

        if (!typeLocked) {
            type = null;
        }
    }

    @Override
    public void readCustomNBT(NBTTagCompound compound) {
        super.readCustomNBT(compound);
        typeLocked = compound.getBoolean("typeLocked");
        charges = compound.getInteger("charges");
        if (compound.hasKey("type")) {
            type = DragonType.valueOf(compound.getString("type"));
        }
    }

    @Override
    public void writeCustomNBT(NBTTagCompound compound) {
        super.writeCustomNBT(compound);
        compound.setBoolean("typeLocked", typeLocked);
        compound.setInteger("charges", charges);
        if (type != null) {
            compound.setString("type", type.name());
        }
    }

    private boolean isFull() {
        return charges >= MMCEAConfig.dragonBreathChargesCapacity;
    }

    @Override
    public void update() {
        lureDragons();
    }
}
