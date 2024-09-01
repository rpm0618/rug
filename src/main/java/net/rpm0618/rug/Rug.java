package net.rpm0618.rug;

import net.rpm0618.rug.rules.RuleManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.ornithemc.osl.entrypoints.api.ModInitializer;

public class Rug implements ModInitializer {

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod name as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LogManager.getLogger("Rug");

	public static final String VERSION = "0.4.0";

    @Override
    public void init() {
        LOGGER.info("Initializing Rug");
		RuleManager.init();
    }
}
