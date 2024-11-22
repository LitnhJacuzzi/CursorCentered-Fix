package org.litnhjacuzzi.cursorcenteredfix;

import java.io.IOException;

import com.sun.jna.Platform;

import net.fabricmc.api.ClientModInitializer;

public class CursorCenteredFix implements ClientModInitializer
{
	public static final boolean IS_WAYLAND;
	
	@Override
	public void onInitializeClient() {
		if(IS_WAYLAND) {
			try {
				new ProcessBuilder("sudo", "ydotoold").start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static {
		if(!Platform.isLinux()) {
			IS_WAYLAND = false;
		}else {
			String displayProt = System.getenv("XDG_SESSION_TYPE");
			IS_WAYLAND = displayProt != null && displayProt.equals("wayland");
		}
	}
}
