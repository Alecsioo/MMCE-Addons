package github.alecsio.mmceaddons.common.item;

import github.alecsio.mmceaddons.CommonProxy;
import github.alecsio.mmceaddons.common.config.MMCEAConfig;
import github.alecsio.mmceaddons.common.registry.RegistryItems;
import mcp.MethodsReturnNonnullByDefault;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemInactiveRadiationSponge extends Item {

    private static final int MAX_RADS_ABSORBED = MMCEAConfig.spongeMaxRadiation;
    private static final String RADS_ABSORBED = "radsAbsorbed";

    public ItemInactiveRadiationSponge() {
        super();
        setMaxStackSize(1);
        setCreativeTab(CommonProxy.creativeTabModularMachineryAddons);
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem entityItem) {
        if (entityItem.world.isRemote) {return super.onEntityItemUpdate(entityItem);}

        entityItem.setNoDespawn();
        Chunk chunk = entityItem.world.getChunk(new BlockPos(entityItem.getPosition()));
        IRadiationSource radiationSource = RadiationHelper.getRadiationSource(chunk);
        if (radiationSource == null) {return super.onEntityItemUpdate(entityItem);}

        double radiationLevel = radiationSource.getRadiationLevel();
        NBTTagCompound tagCompound = entityItem.getItem().getTagCompound();
        if (tagCompound == null) {
            tagCompound = new NBTTagCompound();
            tagCompound.setDouble(RADS_ABSORBED, 0d);
        }

        double radsAbsorbed = tagCompound.getDouble(RADS_ABSORBED);
        radsAbsorbed = Math.min(MAX_RADS_ABSORBED, radsAbsorbed + radiationLevel);
        if (radsAbsorbed >= MAX_RADS_ABSORBED) {
            Entity fluxSponge = new EntityItem(entityItem.getEntityWorld(), entityItem.posX, entityItem.posY, entityItem.posZ, new ItemStack(RegistryItems.radiationSponge));
            entityItem.world.spawnEntity(fluxSponge);
            entityItem.setDead();
        }
        tagCompound.setDouble(RADS_ABSORBED, radsAbsorbed);
        entityItem.getItem().setTagCompound(tagCompound);
        radiationSource.setRadiationLevel(0);
        return super.onEntityItemUpdate(entityItem);
    }
}
