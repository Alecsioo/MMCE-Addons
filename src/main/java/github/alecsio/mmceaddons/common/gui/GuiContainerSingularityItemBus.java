package github.alecsio.mmceaddons.common.gui;

import hellfirepvp.modularmachinery.client.gui.GuiContainerBase;
import hellfirepvp.modularmachinery.common.block.prop.ItemBusSize;
import hellfirepvp.modularmachinery.common.container.ContainerItemBus;
import hellfirepvp.modularmachinery.common.tiles.base.TileItemBus;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiContainerSingularityItemBus extends GuiContainerBase<ContainerItemBus> {
    public GuiContainerSingularityItemBus(TileItemBus itemBus, EntityPlayer opening) {
        super(new ContainerSingularityItemBus(itemBus, opening));
    }

    private ResourceLocation getTextureInventory() {
        ItemBusSize size = this.container.getOwner().getSize();
        return new ResourceLocation("modularmachinery", "textures/gui/inventory_" + size.name().toLowerCase() + ".png");
    }

    protected void setWidthHeight() {
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getTextureInventory());
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }
}
