package website.interactivechat.amwp.handlers.chat;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.MutableText;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import website.interactivechat.amwp.config.Config;

public class PlayerNameHandler {
    public static void handle(ServerPlayerEntity sender, String message) {
        if (sender.getServer() == null) return;

        Config config = Config.getInstance();
        
        // Create base message
        MutableText finalMessage = Text.literal("");

        // Format the name with LuckyPerms data
        LuckPermsData lpData = getLuckPermsData(sender);
        MutableText formattedName = formatPlayerName(sender, lpData, config.getNameFormat());

        // Add formatted name and separator
        finalMessage.append(formattedName)
                   .append(Text.literal(" >> ").formatted(getFormatting(config.getSeparatorColor())))
                   .append(Text.literal(" "));

        // Process message content
        String[] words = message.split(" ");
        for (String word : words) {
            ServerPlayerEntity mentionedPlayer = sender.getServer().getPlayerManager().getPlayer(word);
            if (mentionedPlayer != null) {
                LuckPermsData mentionedData = getLuckPermsData(mentionedPlayer);
                finalMessage.append(formatPlayerName(mentionedPlayer, mentionedData, config.getNameFormat())).append(" ");
            } else {
                finalMessage.append(Text.literal(word + " "));
            }
        }

        // Broadcast the formatted message
        sender.getServer().getPlayerManager().broadcast(finalMessage, false);
    }

    private static MutableText formatPlayerName(ServerPlayerEntity player, LuckPermsData lpData, String nameFormat) {
        MutableText nameText = Text.literal("");
        
        // Add prefix if exists
        if (!lpData.prefix.isEmpty()) {
            nameText.append(Text.literal(lpData.prefix).formatted(Formatting.WHITE));
        }
        
        // Add name
        nameText.append(Text.literal(player.getName().getString())
            .formatted(getFormatting(Config.getInstance().getNameColor())));
        
        // Add suffix if exists
        if (!lpData.suffix.isEmpty()) {
            nameText.append(Text.literal(lpData.suffix).formatted(Formatting.WHITE));
        }

        // Add hover text
        return nameText.setStyle(Style.EMPTY
            .withHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                createHoverText(player, lpData)
            ))
        );
    }

    private static class LuckPermsData {
        String prefix = "";
        String suffix = "";
        String group = "";
    }

    private static LuckPermsData getLuckPermsData(ServerPlayerEntity player) {
        LuckPermsData data = new LuckPermsData();
        
        if (!Config.getInstance().isEnableLuckPerms()) {
            return data;
        }

        try {
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUuid());
            if (user != null) {
                data.prefix = user.getCachedData().getMetaData().getPrefix() != null ? 
                        user.getCachedData().getMetaData().getPrefix() : "";
                data.suffix = user.getCachedData().getMetaData().getSuffix() != null ? 
                        user.getCachedData().getMetaData().getSuffix() : "";
                data.group = user.getPrimaryGroup();
            }
        } catch (Exception e) {
            // LuckyPerms not found or error
        }
        return data;
    }

    private static MutableText createHoverText(ServerPlayerEntity player, LuckPermsData lpData) {
        // Create hover text
        MutableText hoverText = Text.literal("")
            .append(Text.literal("Player Info\n").formatted(Formatting.GOLD, Formatting.BOLD));

        if (!lpData.group.isEmpty()) {
            hoverText.append(Text.literal("Rank: ").formatted(Formatting.LIGHT_PURPLE))
                    .append(Text.literal(lpData.group + "\n").formatted(Formatting.WHITE));
        }

        hoverText.append(Text.literal("Health: ").formatted(Formatting.RED))
                .append(Text.literal(String.format("%.1f/20.0\n", player.getHealth())).formatted(Formatting.WHITE))
                .append(Text.literal("Ping: ").formatted(Formatting.GREEN))
                .append(Text.literal(player.networkHandler.getLatency() + "ms\n").formatted(Formatting.WHITE))
                .append(Text.literal("Gamemode: ").formatted(Formatting.AQUA))
                .append(Text.literal(player.interactionManager.getGameMode().getName()).formatted(Formatting.WHITE));

        return hoverText;
    }

    private static Formatting getFormatting(String color) {
        try {
            return Formatting.valueOf(color.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Formatting.WHITE;
        }
    }
}