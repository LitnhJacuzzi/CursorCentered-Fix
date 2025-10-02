package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.litnhjacuzzi.cursorcenteredfix.HandleAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import com.mojang.blaze3d.platform.Window;

@Mixin(Window.class)
public abstract class WindowAccessor implements HandleAccessor {
	@Shadow(aliases = "handle")
	private long window;
	
	@Override
	public long ccf$getHandle() {
		return window;
	}
}
