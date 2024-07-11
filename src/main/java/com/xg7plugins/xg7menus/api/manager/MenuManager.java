package com.xg7plugins.xg7menus.api.manager;

import com.xg7plugins.xg7menus.api.menus.Menu;
import com.xg7plugins.xg7menus.api.utils.Log;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MenuManager {

    /**
     * The inventories that are open by a player
     */
    private static final HashMap<UUID, Menu> activeInventories = new HashMap<>();

    public static void put(Player player, Menu menu) {
        Log.info("Menu of " + player.getName() + " has been registred!");
        activeInventories.put(player.getUniqueId(), menu);
    }
    public static void remove(Player player) {
        Log.info("Menu of " + player.getName() + " has been removed!");
        activeInventories.remove(player.getUniqueId());
    }
    public static Menu getMenuByPlayer(Player player) {
        return activeInventories.get(player.getUniqueId());
    }
    public static Menu getMenuById(String id) {
        return activeInventories.values().stream().filter(menu -> menu.getId().equals(id)).findFirst().orElse(null);
    }


}
