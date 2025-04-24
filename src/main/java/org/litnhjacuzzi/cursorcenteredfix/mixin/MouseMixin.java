package org.litnhjacuzzi.cursorcenteredfix.mixin;

import java.util.function.BiConsumer;

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
	
	private static final BiConsumer<Integer, Integer> setCursorPos;
	
	private static final ProcessBuilder setCursorPosProcBuilder;
	
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
			setCursorPos.accept(window.getX() + window.getWidth() / 2, window.getY() + window.getHeight() / 2);
		}
	}
	
	private static void setCursorPosAbs(int x, int y) {
		double moveScale = CursorCenteredFix.cursorMoveScalingValue;
		setCursorPosProcBuilder.command().set(4, String.valueOf((int) (x * moveScale)));
		setCursorPosProcBuilder.command().set(5, String.valueOf((int) (y * moveScale)));
		runSetCursorPosCommand();
	}
	
	private static void setCursorPosRel(int x, int y) {
		setCursorPosProcBuilder.command().set(4, String.valueOf(Integer.MIN_VALUE));
		setCursorPosProcBuilder.command().set(6, String.valueOf(Integer.MIN_VALUE));
		runSetCursorPosCommand();
		double moveScale = CursorCenteredFix.cursorMoveScalingValue;
		setCursorPosProcBuilder.command().set(4, String.valueOf((int) (x * moveScale)));
		setCursorPosProcBuilder.command().set(6, String.valueOf((int) (y * moveScale)));
		runSetCursorPosCommand();
	}
	
	private static void runSetCursorPosCommand() {
		try {
			setCursorPosProcBuilder.start().waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	static {
		if(CursorCenteredFix.IS_KDE) {
			setCursorPosProcBuilder = new ProcessBuilder("sudo", "ydotool", "mousemove", "-x", "0", "-y", "0");
			setCursorPos = (x, y) -> setCursorPosRel(x, y);
		}else {
			setCursorPosProcBuilder = new ProcessBuilder("sudo", "ydotool", "mousemove", "-a", "0", "0");
			setCursorPos = (x, y) -> setCursorPosAbs(x, y);
		}
	}
}
