package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;

@Mixin(MinecraftClient.class)
public interface MinecraftClientMixin {
	@Accessor("window")
	public Window getWindow();
}
