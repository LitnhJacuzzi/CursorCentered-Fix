package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin
{
	@Inject(method = "setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", 
			at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Mouse;unlockCursor()V"))
	public void setMouseMode(Screen screen, CallbackInfo ci) {
		if(!requiresCentered(screen)) return;
		
		MinecraftClient client = MinecraftClient.getInstance();
		GLFW.glfwSetInputMode(client.getWindow().getHandle(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	private static boolean requiresCentered(Screen screen) {
		return (screen instanceof HandledScreen || screen instanceof GameMenuScreen);
	}
}
