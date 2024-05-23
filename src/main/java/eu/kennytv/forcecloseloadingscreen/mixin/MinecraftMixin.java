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

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import eu.kennytv.forcecloseloadingscreen.JoiningWorldBridgeScreen;
import eu.kennytv.forcecloseloadingscreen.ReconfigBridgeScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.screens.ReceivingLevelScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.ServerReconfigScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {

    @Shadow
    @Nullable
    public ClientLevel level;

    @Shadow
    @Nullable
    public Screen screen;

    @Shadow
    @Nullable
    public abstract ClientPacketListener getConnection();

    @ModifyVariable(at = @At("HEAD"), method = "setScreen", ordinal = 0, argsOnly = true)
    public Screen setScreen(Screen screen) {
        if (screen instanceof ReceivingLevelScreen) {
            return null;
        } else if (screen instanceof ServerReconfigScreen) {
            return new ReconfigBridgeScreen(this.getConnection().getConnection());
        }
        return screen;
    }

    @Redirect(method = "setScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Minecraft;screen:Lnet/minecraft/client/gui/screens/Screen;", opcode = Opcodes.PUTFIELD))
    public void setScreenCancelCloseScreen(Minecraft instance, Screen screen, @Local(argsOnly = true) LocalRef<Screen> screenRef) {
        if (this.screen instanceof JoiningWorldBridgeScreen && screen instanceof JoiningWorldBridgeScreen) {
            screenRef.set(null);
            screen = null;
        }
        if (this.level != null && screen instanceof JoiningWorldBridgeScreen) {
            screenRef.set(null);
            return;
        }
        this.screen = screen;
    }

    @Redirect(method = "setScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MouseHandler;grabMouse()V"))
    public void setScreenCancelGrabMouse(MouseHandler mouseHandler) {
        if (this.screen != null) {
            return;
        }
        mouseHandler.grabMouse();
    }

    @ModifyArg(method = "setLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;updateScreenAndTick(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL), index = 0)
    private Screen setLevelUpdateScreenAndTick(Screen screen) {
        // Make sure we clean up what needs cleaning up, just that we don't set a new screen on server switches within a proxy
        // Can't just set it to null during reconfiguration, so set an empty screen
        return new JoiningWorldBridgeScreen();
    }
}
