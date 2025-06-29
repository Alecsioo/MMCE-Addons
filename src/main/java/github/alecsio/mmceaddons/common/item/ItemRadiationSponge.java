package github.alecsio.mmceaddons.common.item;

import mcp.MethodsReturnNonnullByDefault;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

// TODO: make it optioanl if NC is loaded
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemRadiationSponge extends Item {

    public ItemRadiationSponge() {
        this.maxStackSize = 1;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote) {return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));}

        IEntityRads entityRads = RadiationHelper.getEntityRadiation(playerIn);
        double entityRadsAmount = 0d;
        if (entityRads != null) {
            entityRadsAmount = entityRads.getTotalRads();
            entityRads.setTotalRads(0, false);
        }

        IRadiationSource radiationSource = RadiationHelper.getRadiationSource(worldIn.getChunk(playerIn.getPosition()));
        double chunkRadsLevel = 0d;
        double chunkRadsBuffer = 0d;
        if (radiationSource != null) {
            chunkRadsLevel = radiationSource.getRadiationLevel();
            radiationSource.setRadiationLevel(0);

            chunkRadsBuffer = radiationSource.getRadiationBuffer();
            radiationSource.setRadiationBuffer(0);
        }

        playerIn.sendMessage(new TextComponentTranslation("message.modularmachineryaddons.radiationsponge.success", entityRadsAmount, playerIn.getName(), chunkRadsBuffer, chunkRadsLevel));
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
