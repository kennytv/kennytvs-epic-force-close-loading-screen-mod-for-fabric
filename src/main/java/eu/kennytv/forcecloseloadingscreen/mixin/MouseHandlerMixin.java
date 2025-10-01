package eu.kennytv.forcecloseloadingscreen.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private boolean mouseGrabbed;

    @Inject(method = "grabMouse", at = @At(value = "HEAD", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"))
    public void fixSprint(CallbackInfo ci) {
        // yarn SystemKeycodes.IS_MAC_OS -> mojang InputQuirks.REPLACE_CTRL_KEY_WITH_CMD_KEY
        if (this.minecraft.isWindowActive() && this.mouseGrabbed && !net.minecraft.client.input.InputQuirks.REPLACE_CTRL_KEY_WITH_CMD_KEY) {
            KeyMapping.setAll();
        }
    }
}
