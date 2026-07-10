package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.litnhjacuzzi.cursorcenteredfix.CursorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHelper;

@Mixin(MouseHelper.class)
public abstract class MouseHandlerMixin {
	@Shadow
	private Minecraft minecraft;
	
	@Inject(method = "releaseMouse()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/client/util/InputMappings;grabOrReleaseMouse(JIDD)V"))
	public void setMouseMode(CallbackInfo ci) {
		CursorManager.applyUnlockedMouseMode(minecraft.getWindow().getWindow());
	}
	
	@Inject(method = "releaseMouse()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/client/util/InputMappings;grabOrReleaseMouse(JIDD)V", shift = At.Shift.AFTER))
	public void centerWaylandCursor(CallbackInfo ci) {
		if (CursorManager.IS_WAYLAND) {
			MainWindow window = minecraft.getWindow();
			CursorManager.centerWaylandCursor(window.getX() + window.getScreenWidth() / 2, window.getY() + window.getScreenHeight() / 2);
		}
	}
}