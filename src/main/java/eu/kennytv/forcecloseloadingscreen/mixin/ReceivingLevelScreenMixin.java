package eu.kennytv.forcecloseloadingscreen.mixin;

import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ReceivingLevelScreen.class)
public abstract class ReceivingLevelScreenMixin extends Screen {

    @Shadow
    private boolean oneTickSkipped;

    protected ReceivingLevelScreenMixin(final Component component) {
        super(component);
    }

    @Inject(at = @At("HEAD"), method = "loadingPacketsReceived")
    public void tick(final CallbackInfo ci) {
        this.oneTickSkipped = true;
    }
}
