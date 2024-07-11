package com.xg7plugins.xg7menus.api.manager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class StorageMenuManager {
    /**
     * The inventories that are open by a player
     */
    @Getter
    private static final List<UUID> usingStorageInventory = new ArrayList<>();

    public static void add(Player player) {
        usingStorageInventory.add(player.getUniqueId());
    }
    public static void remove(Player player) {
        usingStorageInventory.remove(player.getUniqueId());
    }
}
