package com.xg7plugins.xg7menus.api.gui.menus;

import org.bukkit.entity.Player;

import java.util.*;


/**
 * MenuPages is a simple way to create paginated menus. <br>
 * You can create as many menus as you want and open <br>
 * them using their IDs
 */
public class MenuPages {

    /**
     * The menus with ids
     */
    private final HashMap<String, Menu> menus = new HashMap<>();

    public MenuPages addPage(String id, Menu menu) {
        menus.put(id,menu);
        return this;
    }
    public Menu getMenu(String id) {
        return menus.get(id);
    }
    public void openMenu(String id, Player player) {
        getMenu(id).open(player);
    }

}
