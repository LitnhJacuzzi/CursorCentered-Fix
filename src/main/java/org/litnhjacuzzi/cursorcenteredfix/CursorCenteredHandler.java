package org.litnhjacuzzi.cursorcenteredfix;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cursorcenteredfix", value = Dist.CLIENT)
public class CursorCenteredHandler 
{
	@SubscribeEvent
	public static void onGuiOpen(ScreenEvent.Opening event) {
		if(!requiresCentered(event.getNewScreen())) return;
		
		Minecraft mc = Minecraft.getInstance();
		GLFW.glfwSetInputMode(mc.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	private static boolean requiresCentered(Screen screen) {
		return (screen instanceof AbstractContainerScreen || screen instanceof PauseScreen);
	}
}
