package org.litnhjacuzzi.cursorcenteredfix;

import com.sun.jna.Platform;

import net.fabricmc.api.ClientModInitializer;

public class CursorCenteredFix implements ClientModInitializer
{
	public static final boolean IS_WAYLAND;
	
	@Override
	public void onInitializeClient() {}
	
	static {
		if(!Platform.isLinux()) {
			IS_WAYLAND = false;
		}else {
			String displayProt = System.getenv("XDG_SESSION_TYPE");
			IS_WAYLAND = displayProt != null && displayProt.equals("wayland");
		}
	}
}
