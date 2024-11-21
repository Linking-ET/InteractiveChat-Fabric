package website.interactivechat.amwp;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import website.interactivechat.amwp.commands.CommandRegistry;
import website.interactivechat.amwp.config.Config;
import website.interactivechat.amwp.handlers.ChatEventHandler;

public class InteractiveChat implements ModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("InteractiveChat");

	public void onInitialize() {
		LOGGER.info("Initializing InteractiveChat...");
		Config.getInstance();
		CommandRegistry.registerCommands();
		ChatEventHandler.registerEvents();
		LOGGER.info("InteractiveChat initialized successfully!");
	}
}