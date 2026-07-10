package org.litnhjacuzzi.cursorcenteredfix.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.mojang.blaze3d.platform.Window;

import net.minecraft.client.Minecraft;

@Mixin(Minecraft.class)
public interface MinecraftClientAccessor {
	@Accessor("window")
	Window getWindow();
}