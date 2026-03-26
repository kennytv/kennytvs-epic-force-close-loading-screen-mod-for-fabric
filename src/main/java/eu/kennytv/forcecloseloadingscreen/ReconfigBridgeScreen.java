package eu.kennytv.forcecloseloadingscreen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;

public final class ReconfigBridgeScreen extends Screen {
    private final Connection connection;

    public ReconfigBridgeScreen(final Connection connection) {
        super(Component.literal("weee"));
        this.connection = connection;
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, CapturedFrame.CAPTURED_FRAME_ID, 0, 0, 0F, 0F, this.width, this.height, this.width, this.height);
    }

    @Override
    public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
    }

    @Override
    public void tick() {
        // For some reason connection ticking is done in the screen
        // This may trap you on the server if it never completes the reconfiguration phase, the original screen contains a disconnect button
        if (this.connection.isConnected()) {
            this.connection.tick();
        } else {
            this.connection.handleDisconnection();
        }
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
}
