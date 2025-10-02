package org.litnhjacuzzi.cursorcenteredfix;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = "cursorcenteredfix", dist = Dist.CLIENT)
public class CursorCenteredFix {
	
	public static final Logger LOGGER = LogManager.getLogger("cursorcenteredfix");
	
    public CursorCenteredFix(ModContainer container) {
       CursorManager.initialize(LOGGER);
    }
}
