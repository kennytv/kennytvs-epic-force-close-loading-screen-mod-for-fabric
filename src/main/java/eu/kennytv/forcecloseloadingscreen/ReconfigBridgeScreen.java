package eu.kennytv.forcecloseloadingscreen;

import com.mojang.blaze3d.GpuFormat;
import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;

public final class ReconfigBridgeScreen extends Screen {
    // Copied from RenderPipelines.GUI_TEXTURED, except it's not translucent to not break leaves (surely there's a better way?)
    private static final RenderPipeline CAPTURED_FRAME_PIPELINE = RenderPipeline.builder()
            .withBindGroupLayout(BindGroupLayouts.GLOBALS)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withVertexShader("core/position_tex_color")
            .withFragmentShader("core/position_tex_color")
            .withBindGroupLayout(BindGroupLayouts.SAMPLER0)
            .withColorTargetState(new ColorTargetState(Optional.empty(), GpuFormat.RGBA8_UNORM, 15))
            .withVertexBinding(0, DefaultVertexFormat.POSITION_TEX_COLOR)
            .withPrimitiveTopology(PrimitiveTopology.QUADS)
            .withLocation("pipeline/forcecloseloadingscreen_captured_frame")
            .build();
    private final Connection connection;

    public ReconfigBridgeScreen(final Connection connection) {
        super(Component.literal("weee"));
        this.connection = connection;
    }

    @Override
    public void extractRenderState(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
        // Make sure it's not slightly offset or zoomed in (surely there's a better way?)
        final int textureWidth = CapturedFrame.width();
        final int textureHeight = CapturedFrame.height();
        final Minecraft minecraft = Minecraft.getInstance();
        final int guiScale = minecraft.getWindow().getGuiScale();
        final int framebufferWidth = minecraft.getWindow().getWidth();
        final int framebufferHeight = minecraft.getWindow().getHeight();
        if (framebufferWidth <= 0 || framebufferHeight <= 0) {
            return;
        }

        final int sourceWidth = Math.round((float) textureWidth * this.width * guiScale / framebufferWidth);
        final int sourceHeight = Math.round((float) textureHeight * this.height * guiScale / framebufferHeight);
        graphics.fill(0, 0, this.width, this.height, CapturedFrame.backgroundColor());
        graphics.blit(CAPTURED_FRAME_PIPELINE, CapturedFrame.CAPTURED_FRAME_ID, 0, 0, 0.0F, textureHeight, this.width, this.height, sourceWidth, -sourceHeight, textureWidth, textureHeight);
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
