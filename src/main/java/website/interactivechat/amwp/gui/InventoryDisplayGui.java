package website.interactivechat.amwp.gui;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.entity.player.PlayerEntity;

public class InventoryDisplayGui {
    public static void show(ServerPlayerEntity viewer, ServerPlayerEntity target) {
        SimpleInventory inv = createInventory(target);
        SimpleNamedScreenHandlerFactory factory = createScreenHandler(inv, target);
        viewer.openHandledScreen(factory);
    }

    private static SimpleInventory createInventory(ServerPlayerEntity target) {
        SimpleInventory inv = new SimpleInventory(54) {
            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return true;
            }
        };

        // First row (0-8): Player info, Armor, and offhand
        // Player head (slot 0)
        ItemStack headItem = new ItemStack(Items.PLAYER_HEAD);
        inv.setStack(0, headItem);

        // Level display (slot 1)
        ItemStack levelItem = new ItemStack(Items.EXPERIENCE_BOTTLE, target.experienceLevel);
        inv.setStack(1, levelItem);

        // Glass pane for empty slot (slot 2)
        ItemStack emptySlot = new ItemStack(Items.GRAY_STAINED_GLASS_PANE);
        inv.setStack(2, emptySlot);

        // Armor slots (3-6)
        for (int i = 0; i < 4; i++) {
            inv.setStack(i + 3, target.getInventory().getArmorStack(3 - i).copy());
        }

        // Glass pane for empty slot (slot 7)
        inv.setStack(7, emptySlot.copy());

        // Offhand slot (slot 8)
        inv.setStack(8, target.getOffHandStack().copy());

        // Add dividers in the second row (9-17)
        for (int i = 9; i < 18; i++) {
            inv.setStack(i, emptySlot.copy());
        }

        // Main inventory (18-44)
        for (int i = 0; i < 27; i++) {
            ItemStack stack = target.getInventory().getStack(i + 9).copy();
            if (!stack.isEmpty()) {
                inv.setStack(i + 18, stack);
            }
        }

        // Hotbar (45-53)
        for (int i = 0; i < 9; i++) {
            ItemStack stack = target.getInventory().getStack(i).copy();
            if (!stack.isEmpty()) {
                inv.setStack(i + 45, stack);
            }
        }

        return inv;
    }

    private static SimpleNamedScreenHandlerFactory createScreenHandler(SimpleInventory inv, ServerPlayerEntity target) {
        return new SimpleNamedScreenHandlerFactory(
            (syncId, playerInv, playerEntity) -> new ProtectedGenericContainer(
                ScreenHandlerType.GENERIC_9X6, syncId, playerInv, inv, 6
            ),
            Text.literal(target.getName().getString() + "'s Inventory - Level: " + target.experienceLevel)
        );
    }
} 