package org.litnhjacuzzi.cursorcenteredfix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fabricmc.api.ClientModInitializer;

public class CursorCenteredFix implements ClientModInitializer {
	
	public static final Logger LOGGER = LogManager.getLogger("cursorcenteredfix");

	@Override
	public void onInitializeClient() {
		CursorManager.initialize(LOGGER);
	}
}