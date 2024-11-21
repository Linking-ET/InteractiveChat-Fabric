package website.interactivechat.amwp.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import website.interactivechat.amwp.gui.InventoryDisplayGui;

public class ViewInventoryCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("viewinv")
            .then(CommandManager.argument("player", StringArgumentType.word())
                .executes(context -> {
                    try {
                        String playerName = StringArgumentType.getString(context, "player");
                        ServerPlayerEntity viewer = context.getSource().getPlayer();
                        ServerPlayerEntity target = context.getSource().getServer()
                            .getPlayerManager().getPlayer(playerName);
                        
                        if (target != null && viewer != null) {
                            InventoryDisplayGui.show(viewer, target);
                        } else {
                            context.getSource().sendError(Text.literal("Player not found!"));
                        }
                    } catch (Exception e) {
                        context.getSource().sendError(Text.literal("Failed to show inventory"));
                    }
                    return 1;
                })));
    }
} 