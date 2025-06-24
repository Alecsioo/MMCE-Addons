package github.alecsio.mmceaddons.common.item;

import github.alecsio.mmceaddons.CommonProxy;
import mcp.MethodsReturnNonnullByDefault;
import nc.capability.radiation.entity.IEntityRads;
import nc.capability.radiation.source.IRadiationSource;
import nc.network.PacketHandler;
import nc.network.radiation.PlayerRadsUpdatePacket;
import nc.radiation.RadiationHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class ItemRadiationSponge extends Item {

    public ItemRadiationSponge() {
        super();
        setMaxStackSize(1);
        setCreativeTab(CommonProxy.creativeTabModularMachineryAddons);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TextComponentTranslation("tooltip.modularmachineryaddons.radiationsponge").getFormattedText());
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (worldIn.isRemote) {return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));}

        IEntityRads entityRads = RadiationHelper.getEntityRadiation(playerIn);
        if (entityRads == null) {return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));}

        double entityRadsAmount = entityRads.getTotalRads();
        entityRads.setTotalRads(0, false);

        IRadiationSource radiationSource = RadiationHelper.getRadiationSource(worldIn.getChunk(playerIn.getPosition()));
        double chunkRadsLevel = 0d;
        double chunkRadsBuffer = 0d;
        if (radiationSource != null) {
            chunkRadsLevel = radiationSource.getRadiationLevel();
            radiationSource.setRadiationLevel(0);

            chunkRadsBuffer = radiationSource.getRadiationBuffer();
            radiationSource.setRadiationBuffer(0);
        }

        PacketHandler.instance.sendTo(new PlayerRadsUpdatePacket(entityRads), ((EntityPlayerMP) playerIn));
        playerIn.sendMessage(new TextComponentTranslation("message.modularmachineryaddons.radiationsponge.success", ((int) entityRadsAmount), playerIn.getName(), ((int) chunkRadsBuffer), ((int) chunkRadsLevel)));
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
    }
}
