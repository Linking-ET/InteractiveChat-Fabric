package website.interactivechat.amwp.handlers.chat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.ClickEvent;

public class EnderChestChatHandler {
    public static void handle(ServerPlayerEntity player, String originalMessage) {
        MutableText playerName = Text.literal("<" + player.getName().getString() + "> ");
        String beforeEnder = originalMessage.substring(0, originalMessage.indexOf("[ender]"));
        String afterEnder = originalMessage.substring(originalMessage.indexOf("[ender]") + 7);

        MutableText enderDisplay = playerName
            .append(Text.literal(beforeEnder))
            .append(Text.literal("[Ender Chest]")
                .setStyle(Style.EMPTY
                    .withColor(0x9933CC)
                    .withClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/viewender " + player.getName().getString()
                    ))
                ))
            .append(Text.literal(afterEnder));

        player.getServer().getPlayerManager().broadcast(enderDisplay, false);
    }
} 