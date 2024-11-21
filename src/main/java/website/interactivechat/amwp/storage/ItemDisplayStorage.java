package website.interactivechat.amwp.storage;

import net.minecraft.item.ItemStack;
import java.util.HashMap;
import java.util.UUID;

public class ItemDisplayStorage {
    private static final HashMap<UUID, ItemStack> itemDisplays = new HashMap<>();
    
    public static void storeItem(UUID id, ItemStack item) {
        itemDisplays.put(id, item);
    }
    
    public static ItemStack getItem(UUID id) {
        return itemDisplays.get(id);
    }

    public static void removeItem(UUID id) {
        itemDisplays.remove(id);
    }

    public static void clear() {
        itemDisplays.clear();
    }

    public static boolean hasItem(UUID id) {
        return itemDisplays.containsKey(id);
    }
} 