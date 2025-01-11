package eu.kennytv.forcecloseloadingscreen.mixin;

import eu.kennytv.forcecloseloadingscreen.JoiningWorldBridgeScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @ModifyArg(method = "startWaitingForNewLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V", opcode = Opcodes.INVOKEVIRTUAL), index = 0)
    private Screen setLevelUpdateScreenAndTick(Screen screen) {
        return new JoiningWorldBridgeScreen();
    }
}
