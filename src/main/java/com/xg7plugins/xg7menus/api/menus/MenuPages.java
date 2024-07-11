package com.xg7plugins.xg7menus.api.menus;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * MenuPages is a simple way to create paginated menus. <br>
 * You can create as many menus as you want and open <br>
 * them using their IDs
 */
public class MenuPages {

    /**
     * The menus with ids
     */
    private final Map<String, Menu> menus = new HashMap<>();

    public MenuPages(Menu... pages) {
        Arrays.stream(pages).forEach(menu -> menus.put(menu.getId(), menu));
    }

    public Menu getMenu(String id) {
        return menus.get(id);
    }
    public void openMenu(String id, Player player) {
        menus.get(id).open(player);
    }

}
