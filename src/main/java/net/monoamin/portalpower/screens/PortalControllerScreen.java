package net.monoamin.portalpower.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.monoamin.portalpower.menus.PortalControllerMenu;


public class PortalControllerScreen extends AbstractContainerScreen<PortalControllerMenu> {
    private static final ResourceLocation GUI_TEXTURE = new ResourceLocation("portalpower", "textures/gui/resonator_core_gui.png");
    private final int labelX = 30, labelY = 30;
    private Button toggleButton;

    public PortalControllerScreen(PortalControllerMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        /*
         * Renders the background texture to the screen. 'leftPos' and
         * 'topPos' should already represent the top left corner of where
         * the texture should be rendered as it was precomputed from the
         * 'imageWidth' and 'imageHeight'. The two zeros represent the
         * integer u/v coordinates inside the 256 x 256 PNG file.
         */
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        graphics.blit(GUI_TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
        renderPowerLevel(graphics, x, y);
    }

    private void renderPowerLevel(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(GUI_TEXTURE, x + 85, y + 30, 176, 0, 8, menu.getScaledEnergyLevel());
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        super.renderLabels(graphics, mouseX, mouseY);

        // Assume we have some Component 'label'
        // 'label' is drawn at 'labelX' and 'labelY'
        graphics.drawString(this.font, String.valueOf(this.menu.getScaledEnergyLevel()), this.labelX, this.labelY, 0x404040);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        // You can render additional things here if needed (e.g., tooltips)
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    private String getButtonLabel() {
        return menu.isOn() ? "On" : "Off";
    }
}
