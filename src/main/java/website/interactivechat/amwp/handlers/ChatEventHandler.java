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

                Config config = Config.getInstance();

                // Check for [item] keywords
                for (String itemKeyword : config.getItemKeywords()) {
                    if (msg.contains(itemKeyword)) {
                        ItemChatHandler.handle(sender, msg);
                        return false;
                    }
                }

                // Check for [inv] keywords
                for (String invKeyword : config.getInventoryKeywords()) {
                    if (msg.contains(invKeyword)) {
                        InventoryChatHandler.handle(sender, msg);
                        return false;
                    }
                }

                // Check for [ender] keywords
                for (String enderKeyword : config.getEnderChestKeywords()) {
                    if (msg.contains(enderKeyword)) {
                        EnderChestChatHandler.handle(sender, msg);
                        return false;
                    }
                }

                // Handle mentions
                if (msg.contains("@")) {
                    PlayerMentionHandler.handle(sender, msg);
                }
                return true;

            } catch (Exception e) {
                e.printStackTrace();
                return true;
            }
        });
    }
}