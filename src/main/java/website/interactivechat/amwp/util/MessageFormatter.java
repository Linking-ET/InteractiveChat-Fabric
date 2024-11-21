package website.interactivechat.amwp.util;

import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.ClickEvent;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import java.util.UUID;

public class MessageFormatter {
    public static Text formatItemMessage(ServerPlayerEntity player, String originalMessage, ItemStack item, UUID displayId) {
        MutableText playerName = Text.literal("<" + player.getName().getString() + "> ");
        String beforeItem = originalMessage.substring(0, originalMessage.indexOf("[item]"));
        String afterItem = originalMessage.substring(originalMessage.indexOf("[item]") + 6);

        return playerName
            .append(Text.literal(beforeItem))
            .append(item.getName()
                .copy()
                .setStyle(Style.EMPTY
                    .withHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_ITEM,
                        new HoverEvent.ItemStackContent(item)))
                    .withClickEvent(new ClickEvent(
                        ClickEvent.Action.RUN_COMMAND,
                        "/viewitem " + displayId.toString()))))
            .append(Text.literal(afterItem));
    }
} 