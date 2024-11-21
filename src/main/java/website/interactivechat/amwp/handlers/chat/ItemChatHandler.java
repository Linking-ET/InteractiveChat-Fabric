package website.interactivechat.amwp.handlers.chat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.item.ItemStack;
import net.minecraft.text.HoverEvent;
import net.minecraft.util.Formatting;
import website.interactivechat.amwp.storage.ItemDisplayStorage;
import java.util.UUID;

public class ItemChatHandler {
    public static void handle(ServerPlayerEntity player, String originalMessage) {
        ItemStack handItem = player.getMainHandStack();
        if (!handItem.isEmpty()) {
            UUID displayId = UUID.randomUUID();
            ItemDisplayStorage.storeItem(displayId, handItem.copy());
            
            // Create the item display text with count
            MutableText itemText = Text.literal("[")
                .append(Text.literal(handItem.getName().getString())
                    .formatted(Formatting.GOLD)) // Orange color for item name
                .append(Text.literal(" x" + handItem.getCount() + "]")
                    .formatted(Formatting.WHITE));

            // Add hover event to show item tooltip
            itemText.setStyle(itemText.getStyle()
                .withHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_ITEM,
                    new HoverEvent.ItemStackContent(handItem)
                )));

            // Format the complete message
            String beforeItem = originalMessage.substring(0, originalMessage.indexOf("[item]"));
            String afterItem = originalMessage.substring(originalMessage.indexOf("[item]") + 6);

            MutableText finalMessage = Text.literal("<" + player.getName().getString() + "> ")
                .append(Text.literal(beforeItem))
                .append(itemText)
                .append(Text.literal(afterItem));

            // Broadcast the message
            player.getServer().getPlayerManager().broadcast(finalMessage, false);
        }
    }
} 