package org.litnhjacuzzi.cursorcenteredfix;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import com.sun.jna.Platform;

public class CursorManager {
	public static final boolean IS_WAYLAND;
	public static final boolean IS_KDE;
	
	/**wayland scaling factor*/
	public static double cursorMoveScalingValue = 1.0;

	private static final CursorPosController cursorPosController;
	private static final int xArgIndex, yArgIndex;
	private static final ProcessBuilder setCursorPosProcBuilder;
	
	public static void initialize(Logger logger) {
		if(IS_WAYLAND) {
			try {
				new ProcessBuilder("sudo", "ydotoold").start();
				cursorMoveScalingValue = Double.valueOf(System.getProperty("cursorMoveScalingValue"));
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				logger.warn("No valid cursor move scaling value specified, using default value.");
			}
		}
	}
	
	public static void applyUnlockedMouseMode(long handle) {
		GLFW.glfwSetInputMode(handle, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
	}
	
	public static void centerWaylandCursor(int cursorX, int cursorY) {
		cursorPosController.setCursorPos(cursorMoveScalingValue, cursorX, cursorY);
	}
	
	private static void setCursorPosAbs(double moveScale, int x, int y) {
		runSetCursorPosCommand((int) (x * moveScale), (int) (y * moveScale));
	}
	
	private static void setCursorPosRel(double moveScale, int x, int y) {
		runSetCursorPosCommand(Integer.MIN_VALUE, Integer.MIN_VALUE);
		runSetCursorPosCommand((int) (x * moveScale), (int) (y * moveScale));
	}
	
	private static void runSetCursorPosCommand(int x, int y) {
		setCursorPosProcBuilder.command().set(xArgIndex, String.valueOf(x));
		setCursorPosProcBuilder.command().set(yArgIndex, String.valueOf(y));
		try {
			setCursorPosProcBuilder.start().waitFor();
		} catch (Exception e) {
			e.printStackTrace();
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
		
		if(IS_WAYLAND) {
			xArgIndex = 4;
			if(IS_KDE) {
				yArgIndex = 6;
				setCursorPosProcBuilder = new ProcessBuilder("sudo", "ydotool", "mousemove", "-x", "0", "-y", "0");
				cursorPosController = (s, x, y) -> setCursorPosRel(s, x, y);
			}else {
				yArgIndex = 5;
				setCursorPosProcBuilder = new ProcessBuilder("sudo", "ydotool", "mousemove", "-a", "0", "0");
				cursorPosController = (s, x, y) -> setCursorPosAbs(s, x, y);
			}
		}else {
			xArgIndex = 0;
			yArgIndex = 0;
			setCursorPosProcBuilder = null;
			cursorPosController = null;
		}
	}
}
