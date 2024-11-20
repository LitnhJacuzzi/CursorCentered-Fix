package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;

@Mixin(Mouse.class)
public class MouseMixin {
	@Shadow
	private MinecraftClient client;
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/client/util/InputUtil;setCursorParameters(JIDD)V"))
	public void setMouseMode(CallbackInfo ci) {
		GLFW.glfwSetInputMode(((MinecraftClientMixin) client).getWindow().getHandle(), 
				GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
}
