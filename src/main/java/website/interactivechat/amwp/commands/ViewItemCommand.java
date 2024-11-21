package website.interactivechat.amwp.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.CommandManager;
import com.mojang.brigadier.arguments.StringArgumentType;
import website.interactivechat.amwp.gui.ItemDisplayGui;
import website.interactivechat.amwp.storage.ItemDisplayStorage;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import java.util.UUID;

public class ViewItemCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(CommandManager.literal("viewitem")
            .then(CommandManager.argument("uuid", StringArgumentType.string())
                .executes(context -> {
                    String uuidString = StringArgumentType.getString(context, "uuid");
                    UUID uuid = UUID.fromString(uuidString);
                    ServerPlayerEntity player = context.getSource().getPlayer();
                    ItemStack item = ItemDisplayStorage.getItem(uuid);

                    if (item != null && player != null) {
                        ItemDisplayGui.show(player, item);
                    }
                    return 1;
                })));
    }
} 