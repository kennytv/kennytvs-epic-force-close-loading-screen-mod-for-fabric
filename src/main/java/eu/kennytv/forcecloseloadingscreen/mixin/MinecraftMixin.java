package eu.kennytv.forcecloseloadingscreen.mixin;

import eu.kennytv.forcecloseloadingscreen.ForceCloseLoadingScreenMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    public void setScreen(final Screen screen, final CallbackInfo ci) {
        if (ForceCloseLoadingScreenMod.isInstantClose() && screen instanceof ReceivingLevelScreen) {
            ci.cancel();
            setScreen(null);
        }
    }
}
