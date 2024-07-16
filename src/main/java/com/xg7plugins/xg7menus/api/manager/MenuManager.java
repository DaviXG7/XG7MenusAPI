package com.xg7plugins.xg7menus.api.manager;

import com.xg7plugins.xg7menus.api.menus.Menu;
import com.xg7plugins.xg7menus.api.menus.PlayerMenu;
import com.xg7plugins.xg7menus.api.utils.Log;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class MenuManager {

    /**
     * The inventories that are open by a player
     */
    private static final HashMap<UUID, Menu> activeInventories = new HashMap<>();
    private static final HashMap<UUID, PlayerMenu> activePlayerMenus = new HashMap<>();

    public static PlayerMenu getPlayerMenuByPlayer(Player player) {
        return activePlayerMenus.get(player.getUniqueId());
    }
    public static void putPlayerMenu(Player player, PlayerMenu menu) {
        activePlayerMenus.put(player.getUniqueId(), menu);
        Log.info("PlayerMenu of " + player.getName() + " has been registred!");
    }
    public static void removePlayerMenu(Player player) {
        activePlayerMenus.remove(player.getUniqueId());
        Log.info("PlayerMenu of " + player.getName() + " has been removed!");
    }

    public static void put(Player player, Menu menu) {
        activeInventories.put(player.getUniqueId(), menu);
        Log.info("Menu of " + player.getName() + " has been registred!");
    }
    public static void remove(Player player) {
        activeInventories.remove(player.getUniqueId());
        Log.info("Menu of " + player.getName() + " has been removed!");
    }
    public static Menu getMenuByPlayer(Player player) {
        return activeInventories.get(player.getUniqueId());
    }
    public static Menu getMenuById(String id) {
        return activeInventories.values().stream().filter(menu -> menu.getId().equals(id)).findFirst().orElse(null);
    }


}