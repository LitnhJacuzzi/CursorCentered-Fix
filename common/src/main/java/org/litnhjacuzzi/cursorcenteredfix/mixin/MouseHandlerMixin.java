package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.litnhjacuzzi.cursorcenteredfix.CursorManager;
import org.litnhjacuzzi.cursorcenteredfix.HandleAccessor;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
	@Shadow
	private Minecraft minecraft;
	
	@Unique
	private boolean ccf$grabbed = false;
	
	@Inject(method = "releaseMouse()V", at = @At(value = "FIELD", 
			target = "Lnet/minecraft/client/MouseHandler;ypos:D", 
			opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
	public void setMouseMode(CallbackInfo ci) {
		ccf$grabbed = true;
		CursorManager.applyUnlockedMouseMode(((HandleAccessor) (Object) ((MinecraftClientAccessor) minecraft).getWindow()).ccf$getHandle());
	}
	
	@Inject(method = "releaseMouse()V", at = @At("TAIL"))
	public void centerWaylandCursor(CallbackInfo ci) {
		if (ccf$grabbed) {
			if (CursorManager.IS_WAYLAND) {
				Window window = ((MinecraftClientAccessor) minecraft).getWindow();
				CursorManager.centerWaylandCursor(window.getX() + window.getScreenWidth() / 2, window.getY() + window.getScreenHeight() / 2);
			}
			ccf$grabbed = false;
		}
	}
}