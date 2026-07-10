package org.litnhjacuzzi.cursorcenteredfix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;

@Mod("cursorcenteredfix")
public class CursorCenteredFix {

	public static final Logger LOGGER = LogManager.getLogger("cursorcenteredfix");

	public CursorCenteredFix() {
		CursorManager.initialize(LOGGER);
	}
}
