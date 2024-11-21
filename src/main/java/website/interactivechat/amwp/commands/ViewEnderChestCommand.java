package website.interactivechat.amwp.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import website.interactivechat.amwp.gui.EnderChestDisplayGui;

public class ViewEnderChestCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("viewender")
            .then(CommandManager.argument("player", StringArgumentType.string())
                .executes(context -> {
                    String playerName = StringArgumentType.getString(context, "player");
                    ServerPlayerEntity viewer = context.getSource().getPlayer();
                    ServerPlayerEntity target = context.getSource().getServer()
                        .getPlayerManager().getPlayer(playerName);

                    if (target != null && viewer != null) {
                        EnderChestDisplayGui.show(viewer, target);
                    } else {
                        context.getSource().sendError(Text.literal("Player not found!"));
                    }
                    return 1;
                })));
    }
} 