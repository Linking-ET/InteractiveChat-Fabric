package website.interactivechat.amwp.handlers.chat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.ClickEvent;

public class InventoryChatHandler {
    public static void handle(ServerPlayerEntity player, String originalMessage) {
        MutableText playerName = Text.literal("<" + player.getName().getString() + "> ");
        String beforeInv = originalMessage.substring(0, originalMessage.indexOf("[inv]"));
        String afterInv = originalMessage.substring(originalMessage.indexOf("[inv]") + 5);
        
        MutableText invDisplay = playerName
            .append(Text.literal(beforeInv))
            .append(Text.literal("[Inventory]")
                .setStyle(Style.EMPTY
                    .withColor(0x00FF00)
                    .withClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/viewinv " + player.getName().getString()
                    ))
                    .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        Text.literal("Click to view inventory")
                    ))
                ))
            .append(Text.literal(afterInv));

        player.getServer().getPlayerManager().broadcast(invDisplay, false);
    }
} 