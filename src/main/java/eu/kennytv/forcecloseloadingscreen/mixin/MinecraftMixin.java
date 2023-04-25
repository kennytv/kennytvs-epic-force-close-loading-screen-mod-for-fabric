/*
 * This file is part of ForceCloseWorldLoadingScreen - https://github.com/kennytv/kennytvs-epic-force-close-loading-screen-mod-for-fabric
 * Copyright (C) 2023 Nassim Jahnke
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package eu.kennytv.forcecloseloadingscreen.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    public abstract void setScreen(@Nullable Screen screen);

    @Shadow
    protected abstract void updateScreenAndTick(Screen screen);

    @Shadow
    @Nullable
    public ClientLevel level;

    @Inject(at = @At("HEAD"), method = "setScreen", cancellable = true)
    public void setScreen(final Screen screen, final CallbackInfo ci) {
        if (screen instanceof ReceivingLevelScreen) {
            ci.cancel();
            this.setScreen(null);
        }
    }

    @Redirect(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL))
    private void updateScreenAndTick(final Minecraft instance, final Screen screen) {
        // Make sure we clean up what needs cleaning up, just that we don't set a new screen on server switches within a proxy
        this.updateScreenAndTick(this.level != null ? null : screen);
    }
}
