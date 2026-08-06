package github.alecsio.mmceaddons.common.hatch.vanilla.gui;

import github.alecsio.mmceaddons.util.SingularitySlotItemHandler;
import hellfirepvp.modularmachinery.client.gui.GuiContainerBase;
import hellfirepvp.modularmachinery.common.block.prop.ItemBusSize;
import hellfirepvp.modularmachinery.common.tiles.base.TileItemBus;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class GuiContainerSingularityItemBus extends GuiContainerBase<ContainerSingularityItemBus> {
    private static CompactCountRenderItem compactCountRenderItem;
    private static final NumberFormat EXACT_COUNT_FORMAT = NumberFormat.getIntegerInstance(Locale.US);
    public GuiContainerSingularityItemBus(TileItemBus itemBus, EntityPlayer opening) {
        super(new ContainerSingularityItemBus(itemBus, opening));
    }
    @Override
    public void initGui() {
        super.initGui();
        RenderItem vanillaRenderItem = this.mc.getRenderItem();
        if (compactCountRenderItem == null || !compactCountRenderItem.wraps(vanillaRenderItem)) {
            compactCountRenderItem = new CompactCountRenderItem(this.mc, vanillaRenderItem);}
        this.itemRender = compactCountRenderItem;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        compactCountRenderItem.setContainer(this.container);
        try {super.drawScreen(mouseX, mouseY, partialTicks);
        } finally {compactCountRenderItem.clearContainer(this.container);
        }
    }
    private ResourceLocation getTextureInventory() {
        ItemBusSize size = this.container.getOwner().getSize();
        return new ResourceLocation("modularmachinery", "textures/gui/inventory_" + size.name().toLowerCase() + ".png");
    }

    protected void setWidthHeight() {}
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(this.getTextureInventory());
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
    }

    static String formatCompactCount(int count) {
        if (count < 1_000) {return Integer.toString(count);}
        int divisor;
        char suffix;
        if (count < 1_000_000) {
            divisor = 1_000;
            suffix = 'k';
        } else if (count < 1_000_000_000) {
            divisor = 1_000_000;
            suffix = 'M';
        } else {
            divisor = 1_000_000_000;
            suffix = 'B';
        }
        int whole = count / divisor;
        if (whole >= 100) {return Integer.toString(whole) + suffix;}
        int decimal = count % divisor / (divisor / 10);
        if (decimal == 0) {return Integer.toString(whole) + suffix;}
        return Integer.toString(whole) + '.' + decimal + suffix;
    }

    private static final class CompactCountRenderItem extends RenderItem {

        private static final int CACHE_SIZE = ItemBusSize.LUDICROUS.getSlotCount();
        private final RenderItem delegate;
        private final int[] cachedCounts = new int[CACHE_SIZE];
        private final String[] cachedText = new String[CACHE_SIZE];
        private ContainerSingularityItemBus container;

        private CompactCountRenderItem(Minecraft minecraft, RenderItem delegate) {
            super(minecraft.getTextureManager(), delegate.getItemModelMesher().getModelManager(), minecraft.getItemColors());
            this.delegate = delegate;
        }

        private boolean wraps(RenderItem renderItem) {return this.delegate == renderItem;}
        private void setContainer(ContainerSingularityItemBus container) {this.container = container;}
        private void clearContainer(ContainerSingularityItemBus container) {if (this.container == container) {
                this.container = null;}
        }

        @Override
        public void renderItemAndEffectIntoGUI(@Nullable EntityLivingBase entity, ItemStack stack, int x, int y) {
            float previousZLevel = this.delegate.zLevel;
            this.delegate.zLevel = this.zLevel;
            try {
                this.delegate.renderItemAndEffectIntoGUI(entity, stack, x, y);
            } finally {
                this.delegate.zLevel = previousZLevel;
            }
        }

        @Override
        public void renderItemOverlayIntoGUI(FontRenderer fontRenderer, ItemStack stack, int x, int y, @Nullable String text) {
            float previousZLevel = this.delegate.zLevel;
            this.delegate.zLevel = this.zLevel;
            try {
                if (text == null && stack.getCount() >= 1_000 && this.container != null) {
                    int machineSlot = this.container.findRenderedMachineSlot(stack, x, y);
                    if (machineSlot >= 0) {
                        this.delegate.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, "");
                        drawCompactCount(fontRenderer, getCachedText(machineSlot, stack.getCount()), x, y);
                        return;
                    }
                }
                this.delegate.renderItemOverlayIntoGUI(fontRenderer, stack, x, y, text);
            } finally {
                this.delegate.zLevel = previousZLevel;
            }
        }

        private String getCachedText(int machineSlot, int count) {
            if (this.cachedText[machineSlot] == null || this.cachedCounts[machineSlot] != count) {
                this.cachedCounts[machineSlot] = count;
                this.cachedText[machineSlot] = formatCompactCount(count);
            }
            return this.cachedText[machineSlot];
        }

        private static void drawCompactCount(FontRenderer fontRenderer, String text, int x, int y) {
            final float scale = 0.5F;
            final float inverseScale = 1.0F / scale;
            final int offset = -1;
            boolean unicodeFlag = fontRenderer.getUnicodeFlag();
            fontRenderer.setUnicodeFlag(false);
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            GlStateManager.pushMatrix();
            try {
                GlStateManager.scale(scale, scale, scale);
                int drawX = (int) ((x + offset + 16.0F - fontRenderer.getStringWidth(text) * scale) * inverseScale);
                int drawY = (int) ((y + offset + 16.0F - 7.0F * scale) * inverseScale);
                fontRenderer.drawStringWithShadow(text, drawX, drawY, 0xFFFFFF);
            } finally {
                GlStateManager.popMatrix();
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                GlStateManager.enableBlend();
                fontRenderer.setUnicodeFlag(unicodeFlag);
            }
        }
    }
    @Override
    public List<String> getItemToolTip(ItemStack stack) {
        List<String> tooltip = super.getItemToolTip(stack);
        Slot hoveredSlot = this.getSlotUnderMouse();
        if (stack.getCount() >= 1_000
                && hoveredSlot instanceof SingularitySlotItemHandler
                && hoveredSlot.getStack() == stack) {
            tooltip.add(TextFormatting.GRAY + I18n.format("tooltip.modularmachineryaddons.singularity.stored", EXACT_COUNT_FORMAT.format(stack.getCount())));
        }
        return tooltip;
    }
}