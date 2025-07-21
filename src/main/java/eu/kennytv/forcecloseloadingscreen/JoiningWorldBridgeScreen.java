package eu.kennytv.forcecloseloadingscreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class JoiningWorldBridgeScreen extends Screen {

    public JoiningWorldBridgeScreen() {
        super(Component.literal("wooo"));
    }

    @Override
    public void render(final GuiGraphics guiGraphics, final int i, final int j, final float f) {
        if (CapturedFrame.capturedTexture() != null) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, CapturedFrame.CAPTURED_FRAME_ID, 0, 0, 0.0F, 0.0F, this.width, this.height, this.width, this.height);
        }
    }

    @Override
    public void renderBackground(final GuiGraphics guiGraphics, final int i, final int j, final float f) {
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    protected boolean shouldNarrateNavigation() {
        return false;
    }

    @Override
    public void removed() {
        CapturedFrame.clearCapturedTexture();
        super.removed();
    }
}
