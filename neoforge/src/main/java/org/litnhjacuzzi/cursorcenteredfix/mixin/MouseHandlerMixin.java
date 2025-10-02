package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.litnhjacuzzi.cursorcenteredfix.CursorManager;
import org.litnhjacuzzi.cursorcenteredfix.HandleAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
	private static final String old_injection_target = 
			"Lcom/mojang/blaze3d/platform/InputConstants;grabOrReleaseMouse(JIDD)V";
	private static final String injection_target = 
			"Lcom/mojang/blaze3d/platform/InputConstants;grabOrReleaseMouse(Lcom/mojang/blaze3d/platform/Window;IDD)V";
	
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method = "releaseMouse()V", at = @At(value = "INVOKE", target = old_injection_target), require = 0)
	public void setMouseModeLegacy(CallbackInfo ci) {
		CursorManager.applyUnlockedMouseMode(((HandleAccessor) (Object) minecraft.getWindow()).ccf$getHandle());
	}
	
	@Inject(method = "releaseMouse()V", at = @At(value = "INVOKE", target = injection_target), require = 0)
	public void setMouseMode(CallbackInfo ci) {
		CursorManager.applyUnlockedMouseMode(minecraft.getWindow().handle());
	}
	
	@Inject(method = "releaseMouse()V", at = @At(value = "INVOKE", target = old_injection_target, shift = At.Shift.AFTER), require = 0)
	public void centerWaylandCursorLegacy(CallbackInfo ci) {
		ccf$centerWaylandCursor();
	}
	
	@Inject(method = "releaseMouse()V", at = @At(value = "INVOKE", target = injection_target, shift = At.Shift.AFTER), require = 0)
	public void centerWaylandCursor(CallbackInfo ci) {
		ccf$centerWaylandCursor();
	}
	
	private void ccf$centerWaylandCursor() {
		if(CursorManager.IS_WAYLAND) {
			Window window = minecraft.getWindow();
			CursorManager.centerWaylandCursor(window.getX() + window.getWidth() / 2, window.getY() + window.getHeight() / 2);
		}
	}
}
