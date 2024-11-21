package website.interactivechat.amwp.handlers.chat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

public class PlayerNameHandler {
    public static void handle(ServerPlayerEntity sender, String message) {
        if (sender.getServer() == null) return;

        // Create base message starting with player name
        MutableText finalMessage = Text.literal("");

        // Format the sender's name with hover info
        MutableText senderName = formatPlayerName(sender);
        finalMessage.append(Text.literal("<").formatted(Formatting.WHITE))
                   .append(senderName)
                   .append(Text.literal("> ").formatted(Formatting.WHITE));

        // Split message and check for player names
        String[] words = message.split(" ");
        for (String word : words) {
            ServerPlayerEntity mentionedPlayer = sender.getServer().getPlayerManager().getPlayer(word);
            if (mentionedPlayer != null) {
                // If word is a player name, format it with hover info
                finalMessage.append(formatPlayerName(mentionedPlayer)).append(" ");
            } else {
                // Regular word
                finalMessage.append(Text.literal(word + " "));
            }
        }

        // Broadcast the formatted message
        sender.getServer().getPlayerManager().broadcast(finalMessage, false);
    }

    private static MutableText formatPlayerName(ServerPlayerEntity player) {
        // Create hover text with player info
        MutableText hoverText = Text.literal("")
            .append(Text.literal("Player Info\n").formatted(Formatting.GOLD, Formatting.BOLD))
            .append(Text.literal("Health: ").formatted(Formatting.RED))
            .append(Text.literal(String.format("%.1f/20.0\n", player.getHealth())).formatted(Formatting.WHITE))
            .append(Text.literal("Ping: ").formatted(Formatting.GREEN))
            .append(Text.literal(player.networkHandler.getLatency() + "ms\n").formatted(Formatting.WHITE))
            .append(Text.literal("Gamemode: ").formatted(Formatting.AQUA))
            .append(Text.literal(player.interactionManager.getGameMode().getName()).formatted(Formatting.WHITE));

        // Create player name text with hover event
        return Text.literal(player.getName().getString())
            .setStyle(Style.EMPTY
                .withColor(Formatting.YELLOW)
                .withHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    hoverText
                ))
            );
    }
} 