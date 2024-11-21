package website.interactivechat.amwp.gui;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;

public class EnderChestDisplayGui {
    public static void show(ServerPlayerEntity viewer, ServerPlayerEntity target) {
        SimpleInventory inv = createInventory(target);
        SimpleNamedScreenHandlerFactory factory = createScreenHandler(inv, target);
        viewer.openHandledScreen(factory);
    }

    private static SimpleInventory createInventory(ServerPlayerEntity target) {
        SimpleInventory inv = new SimpleInventory(27) {
            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return true;
            }
        };

        // Copy ender chest contents
        Inventory enderChest = target.getEnderChestInventory();
        for (int i = 0; i < enderChest.size(); i++) {
            inv.setStack(i, enderChest.getStack(i).copy());
        }

        return inv;
    }

    private static SimpleNamedScreenHandlerFactory createScreenHandler(SimpleInventory inv, ServerPlayerEntity target) {
        return new SimpleNamedScreenHandlerFactory(
            (syncId, playerInv, playerEntity) -> new ProtectedGenericContainer(
                ScreenHandlerType.GENERIC_9X3, syncId, playerInv, inv, 3
            ),
            Text.literal(target.getName().getString() + "'s Ender Chest")
        );
    }
} 