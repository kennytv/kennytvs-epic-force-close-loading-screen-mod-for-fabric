package eu.kennytv.forcecloseloadingscreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.Identifier;

public final class CapturedFrame {

    public static final Identifier CAPTURED_FRAME_ID = Identifier.fromNamespaceAndPath("forcecloseloadingscreen", "captured_frame");
    public static boolean initialJoin = true;

    public static void clearCapturedTexture() {
        Minecraft.getInstance().getTextureManager().release(CAPTURED_FRAME_ID);
    }

    public static void captureLastFrame() {
        // I'm fairly sure this isn't the best way to do this, but it's the only way I know how to do
        final Minecraft client = Minecraft.getInstance();
        Screenshot.takeScreenshot(client.getMainRenderTarget(), nativeImage -> {
            client.getTextureManager().register(CapturedFrame.CAPTURED_FRAME_ID, new DynamicTexture(null, nativeImage));
        });
    }
}
