package eu.kennytv.forcecloseloadingscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

public final class CapturedFrame {

    public static final ResourceLocation CAPTURED_FRAME_ID = ResourceLocation.fromNamespaceAndPath("forcecloseloadingscreen", "captured_frame");
    public static boolean initialJoin = true;
    private static DynamicTexture capturedTexture;

    public static DynamicTexture capturedTexture() {
        return capturedTexture;
    }

    public static void clearCapturedTexture() {
        if (capturedTexture != null) {
            capturedTexture.close();
            Minecraft.getInstance().getTextureManager().release(CAPTURED_FRAME_ID);
            capturedTexture = null;
        }
    }

    public static void captureLastFrame() {
        // I'm fairly sure this isn't the best way to do this, but it's the only way I know how to do
        final Minecraft client = Minecraft.getInstance();
        Screenshot.takeScreenshot(client.getMainRenderTarget(), nativeImage -> {
            capturedTexture = new DynamicTexture(null, nativeImage);
            client.getTextureManager().register(CapturedFrame.CAPTURED_FRAME_ID, CapturedFrame.capturedTexture());
        });
    }
}
