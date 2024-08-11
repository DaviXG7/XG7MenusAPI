package com.xg7plugins.xg7menus.api.menus;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * MenuPages is a simple way to create paginated menus. <br>
 * You can create as many menus as you want and open <br>
 * them using their IDs
 */
public class MenuPages {

    /**
     * The menus with ids
     */
    private final List<Menu> menus = new ArrayList<>();

    public MenuPages(Menu... pages) {
        menus.addAll(Arrays.asList(pages));
    }

    public MenuPages addPages(Menu... pages) {
        menus.addAll(Arrays.asList(pages));
        return this;
    }
    public Menu getMenu(String id) {
        return menus.stream().filter(menu -> menu.getId().equals(id)).findFirst().orElse(null);
    }
    public void openMenu(String id, Player player) {
        getMenu(id).open(player);
    }

}
