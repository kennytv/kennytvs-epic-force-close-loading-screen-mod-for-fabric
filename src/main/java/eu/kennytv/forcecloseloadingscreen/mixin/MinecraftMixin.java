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

import eu.kennytv.forcecloseloadingscreen.CapturedFrame;
import eu.kennytv.forcecloseloadingscreen.JoiningWorldBridgeScreen;
import eu.kennytv.forcecloseloadingscreen.ReconfigBridgeScreen;
import eu.kennytv.forcecloseloadingscreen.TitleBridgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.LevelLoadingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    @Nullable
    public abstract ClientPacketListener getConnection();

    @ModifyVariable(at = @At("HEAD"), method = "setScreen", ordinal = 0, argsOnly = true)
    public Screen setScreen(final Screen screen) {
        if (screen instanceof LevelLoadingScreen) {
            if (CapturedFrame.initialJoin) {
                CapturedFrame.initialJoin = false;
                return screen;
            }
            return null;
        } else if (screen instanceof ServerReconfigScreen) {
            return new ReconfigBridgeScreen(this.getConnection().getConnection());
        } else if (screen instanceof TitleScreen) {
            return new TitleBridgeScreen();
        }
        return screen;
    }

//    @ModifyArg(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL), index = 0)
//    private Screen setLevelUpdateScreenAndTick(final Screen screen) {
//        if (CapturedFrame.initialJoin) {
//            return screen;
//        } else {
//            // Make sure we clean up what needs cleaning up, just that we don't set a new screen on server switches within a proxy
//            // Can't just set it to null during reconfiguration, so set an empty screen
//            return new JoiningWorldBridgeScreen(!CapturedFrame.respawn);
//        }
//    }

    @Inject(at = @At("HEAD"), method = "clearClientLevel")
    public void clearClientLevel(final Screen screen, final CallbackInfo ci) {
        // Capture frames for reconfiguration
        CapturedFrame.captureLastFrame();
    }

    @Inject(at = @At("HEAD"), method = "disconnect")
    public void disconnect(final Screen screen, final boolean bl, final CallbackInfo ci) {
        CapturedFrame.initialJoin = true;
        CapturedFrame.clearCapturedTexture();
    }
}
