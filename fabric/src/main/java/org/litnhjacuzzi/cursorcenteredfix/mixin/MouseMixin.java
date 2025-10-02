package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.litnhjacuzzi.cursorcenteredfix.CursorManager;
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
	private static final String old_injection_target = 
			"Lnet/minecraft/class_3675;method_15984(JIDD)V";
	private static final String injection_target = 
			"Lnet/minecraft/client/util/InputUtil;setCursorParameters(Lnet/minecraft/client/util/Window;IDD)V";
	
	@Shadow
	private MinecraftClient client;
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", target = old_injection_target), remap = false, require = 0)
	public void setMouseModeLegacy(CallbackInfo ci) {
		CursorManager.applyUnlockedMouseMode(((MinecraftClientAccessor) client).getWindow().getHandle());
	}
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", target = injection_target), require = 0)
	public void setMouseMode(CallbackInfo ci) {
		CursorManager.applyUnlockedMouseMode(client.getWindow().getHandle());
	}
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", target = old_injection_target), remap = false, require = 0)
	public void centerWaylandCursorLegacy(CallbackInfo ci) {
		cursorcenteredfix$centerWaylandCursor();
	}
	
	@Inject(method = "unlockCursor()V", at = @At(value = "INVOKE", target = injection_target), require = 0)
	public void centerWaylandCursor(CallbackInfo ci) {
		cursorcenteredfix$centerWaylandCursor();
	}
	
	private void cursorcenteredfix$centerWaylandCursor() {
		if(CursorManager.IS_WAYLAND) {
			Window window = ((MinecraftClientAccessor) client).getWindow();
			CursorManager.centerWaylandCursor(window.getX() + window.getWidth() / 2, window.getY() + window.getHeight() / 2);
		}
	}
}