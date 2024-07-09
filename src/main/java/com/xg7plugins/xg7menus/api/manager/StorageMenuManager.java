package com.xg7plugins.xg7menus.api.manager;

import com.xg7plugins.xg7menus.api.menus.Menu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StorageMenuManager {
    @Getter
    private static final List<UUID> usingStorageInventory = new ArrayList<>();

    public static void add(Player player) {
        usingStorageInventory.add(player.getUniqueId());
    }
    public static void remove(Player player) {
        usingStorageInventory.remove(player.getUniqueId());
    }
}
