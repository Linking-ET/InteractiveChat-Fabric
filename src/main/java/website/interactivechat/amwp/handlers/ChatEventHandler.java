package website.interactivechat.amwp.handlers;

import java.util.Objects;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.minecraft.text.Text;
import website.interactivechat.amwp.config.Config;
import website.interactivechat.amwp.handlers.chat.EnderChestChatHandler;
import website.interactivechat.amwp.handlers.chat.InventoryChatHandler;
import website.interactivechat.amwp.handlers.chat.ItemChatHandler;
import website.interactivechat.amwp.handlers.chat.PlayerMentionHandler;
import website.interactivechat.amwp.handlers.chat.PlayerNameHandler;

public class ChatEventHandler {
    public static void registerEvents() {
        ServerMessageEvents.ALLOW_CHAT_MESSAGE.register((message, sender, params) -> {
            try {
                Text content = message.getContent();
                if (content == null) {
                    return true;
                }

                String msg = content.getString();
                if (msg == null || msg.isEmpty()) {
                    return true;
                }

                // Check for special keywords first
                if (msg.contains("[item]")) {
                    ItemChatHandler.handle(sender, msg);
                    return false;
                } else if (msg.contains("[inv]")) {
                    InventoryChatHandler.handle(sender, msg);
                    return false;
                } else if (msg.contains("[ender]")) {
                    EnderChestChatHandler.handle(sender, msg);
                    return false;
                }

                // Handle interactive player names for regular messages
                PlayerNameHandler.handle(sender, msg);
                return false;

            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        });
    }
}