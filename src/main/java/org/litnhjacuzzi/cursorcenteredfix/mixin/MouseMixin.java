package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.litnhjacuzzi.cursorcenteredfix.CursorCenteredFix;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.client.util.Window;

@Mixin(Mouse.class)
public abstract class MouseMixin {
	@Shadow
	private MinecraftClient client;
	
	private final ProcessBuilder setCursorPosProcBuilder = new ProcessBuilder("sudo", "ydotool", "mousemove", "0", "0");
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/client/util/InputUtil;setCursorParameters(JIDD)V"))
	public void setMouseMode(CallbackInfo ci) {
		GLFW.glfwSetInputMode(((MinecraftClientMixin) client).getWindow().getHandle(), 
				GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/client/util/InputUtil;setCursorParameters(JIDD)V", shift = At.Shift.AFTER))
	public void setCursorPos(CallbackInfo ci) {
		if(CursorCenteredFix.IS_WAYLAND) {
			Window window = ((MinecraftClientMixin) client).getWindow();
			String xArg = String.valueOf(window.getX() + window.getWidth() / 2);
			String yArg = String.valueOf(window.getY() + window.getHeight() / 2);
			setCursorPosProcBuilder.command().set(3, xArg);
			setCursorPosProcBuilder.command().set(4, yArg);
			try {
				Process setCursorPosProc = setCursorPosProcBuilder.start();
				setCursorPosProc.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
