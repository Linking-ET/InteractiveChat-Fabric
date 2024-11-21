package website.interactivechat.amwp.gui;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;

public class ItemDisplayGui {
    public static void show(ServerPlayerEntity player, ItemStack item) {
        SimpleInventory inv = createInventory(item);
        SimpleNamedScreenHandlerFactory factory = createScreenHandler(inv, item);
        player.openHandledScreen(factory);
    }

    private static SimpleInventory createInventory(ItemStack item) {
        SimpleInventory inv = new SimpleInventory(9) {
            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return true;
            }
        };
        inv.setStack(4, item.copy());
        return inv;
    }

    private static SimpleNamedScreenHandlerFactory createScreenHandler(SimpleInventory inv, ItemStack item) {
        return new SimpleNamedScreenHandlerFactory(
            (syncId, playerInv, playerEntity) -> new ProtectedGenericContainer(
                ScreenHandlerType.GENERIC_9X1, syncId, playerInv, inv, 1
            ),
            item.getName()
        );
    }
} 