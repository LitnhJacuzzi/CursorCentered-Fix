package org.litnhjacuzzi.cursorcenteredfix;

import org.lwjgl.glfw.GLFW;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.IngameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "cursorcenteredfix", value = Dist.CLIENT)
public class CursorCenteredHandler 
{
	@SubscribeEvent
	public static void onGuiOpen(GuiOpenEvent event) {
		if(!requiresCentered(event.getGui())) return;
		
		Minecraft mc = Minecraft.getInstance();
		GLFW.glfwSetInputMode(mc.getWindow().getWindow(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	private static boolean requiresCentered(Screen screen) {
		return (screen instanceof ContainerScreen || screen instanceof IngameMenuScreen);
	}
}
