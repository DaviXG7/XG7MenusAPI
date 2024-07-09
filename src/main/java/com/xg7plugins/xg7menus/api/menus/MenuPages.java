package com.xg7plugins.xg7menus.api.menus;

import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MenuPages {

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
