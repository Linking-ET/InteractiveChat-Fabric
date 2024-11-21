package website.interactivechat.amwp.handlers.chat;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.sound.SoundEvents;
import net.minecraft.sound.SoundCategory;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import website.interactivechat.amwp.config.Config;

public class PlayerMentionHandler {
    public static void handle(ServerPlayerEntity sender, String message) {
        if (sender.getServer() == null) return;

        Config config = Config.getInstance();

        for (ServerPlayerEntity player : sender.getServer().getPlayerManager().getPlayerList()) {
            String playerName = player.getName().getString();
            if (player == sender) continue;

            if (message.toLowerCase().contains(playerName.toLowerCase())) {
                MutableText notification = Text.literal("You were mentioned by ")
                    .formatted(Formatting.YELLOW)
                    .append(Text.literal(sender.getName().getString())
                        .formatted(Formatting.GOLD));

                // Send title and subtitle using packets
                if (config.isEnableTitle()) {
                    player.networkHandler.sendPacket(new TitleS2CPacket(Text.literal("Mentioned!").formatted(Formatting.RED)));
                }
                if (config.isEnableSubtitle()) {
                    player.networkHandler.sendPacket(new SubtitleS2CPacket(Text.literal("You were mentioned by " + sender.getName().getString()).formatted(Formatting.YELLOW)));
                }
                if (config.isEnableTitle() || config.isEnableSubtitle()) {
                    player.networkHandler.sendPacket(new TitleFadeS2CPacket(10, 70, 20)); // Timing packet
                }

                // Send an action bar message
                if (config.isEnableActionBar()) {
                    player.sendMessage(
                        Text.literal("You were mentioned in chat!").formatted(Formatting.GREEN),
                        true
                    );
                }

                player.sendMessage(notification, false);
                
                // Play sound directly to the mentioned player
                player.playSound(
                    SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
                    1.0f,  // Volume
                    1.0f   // Pitch
                );
                
                // Also play at the player's location for others to hear
                player.getWorld().playSound(
                    null, // No source player
                    player.getX(),
                    player.getY(),
                    player.getZ(),
                    SoundEvents.BLOCK_NOTE_BLOCK_PLING.value(),
                    SoundCategory.PLAYERS,
                    1.0f,  // Volume
                    1.0f   // Pitch
                );
            }
        }
    }

    private static MutableText formatMentionMessage(ServerPlayerEntity sender, String message) {
        MutableText formattedMessage = Text.literal("<" + sender.getName().getString() + "> ");
        String[] words = message.split(" ");
        for (String word : words) {
            ServerPlayerEntity mentionedPlayer = sender.getServer().getPlayerManager().getPlayer(word);
            if (mentionedPlayer != null && mentionedPlayer != sender) {
                formattedMessage.append(Text.literal("@" + word)
                    .formatted(Formatting.YELLOW)
                    .append(" "));
            } else {
                formattedMessage.append(Text.literal(word + " "));
            }
        }
        return formattedMessage;
    }
} 