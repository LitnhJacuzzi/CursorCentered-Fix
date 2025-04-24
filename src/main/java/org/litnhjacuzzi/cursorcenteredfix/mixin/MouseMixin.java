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

	private final ProcessBuilder setCursorPosProcBuilderAbsolute = new ProcessBuilder("sudo", "ydotool", "mousemove", "--absolute", "0", "0");
	private final ProcessBuilder setCursorPosProcBuilderRelative = new ProcessBuilder("sudo", "ydotool", "mousemove", "0", "0");
  
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
			double moveScale = CursorCenteredFix.cursorMoveScalingValue;
			String xArg = String.valueOf((int) ((window.getX() + window.getWidth() / 2) * moveScale));
			String yArg = String.valueOf((int) ((window.getY() + window.getHeight() / 2) * moveScale));
			try {
				// first set cursor to 0 0 to circumvent ydotool bug
				Process setCursorPosProcAbsolute = setCursorPosProcBuilderAbsolute.start();
				setCursorPosProcAbsolute.waitFor();

				// after having cursor at 0 0, go to the correct position
				setCursorPosProcBuilderRelative.command().set(3, xArg);
				setCursorPosProcBuilderRelative.command().set(4, yArg);
				Process setCursorPosProcRelative = setCursorPosProcBuilderRelative.start();
				setCursorPosProcRelative.waitFor();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
