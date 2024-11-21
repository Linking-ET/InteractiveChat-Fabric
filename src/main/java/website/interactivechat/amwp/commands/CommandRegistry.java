package website.interactivechat.amwp.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            ViewItemCommand.register(dispatcher);
            ViewInventoryCommand.register(dispatcher);
            ViewEnderChestCommand.register(dispatcher);
        });
    }
} 