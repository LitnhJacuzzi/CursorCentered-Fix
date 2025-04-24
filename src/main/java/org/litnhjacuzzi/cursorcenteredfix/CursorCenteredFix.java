package org.litnhjacuzzi.cursorcenteredfix;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.jna.Platform;

import net.fabricmc.api.ClientModInitializer;

public class CursorCenteredFix implements ClientModInitializer {
	
	public static final Logger LOGGER = LogManager.getLogger("cursorcenteredfix");
	
	public static final boolean IS_WAYLAND;
	public static final boolean IS_KDE;
	
	/**wayland scaling factor*/
	public static double cursorMoveScalingValue = 1.0;
	
	@Override
	public void onInitializeClient() {
		if(IS_WAYLAND) {
			try {
				new ProcessBuilder("sudo", "ydotoold").start();
				cursorMoveScalingValue = Double.valueOf(System.getProperty("cursorMoveScalingValue"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				LOGGER.warn("No valid cursor move scaling value specified, using default value.");
			}
		}
	}
	
	static {
		if(!Platform.isLinux()) {
			IS_WAYLAND = false;
			IS_KDE = false;
		}else {
			String displayProt = System.getenv("XDG_SESSION_TYPE");
			IS_WAYLAND = displayProt != null && displayProt.equals("wayland");
			String desktop = System.getenv("XDG_CURRENT_DESKTOP");
			IS_KDE = desktop != null && desktop.contains("KDE");
		}
	}
}
