package com.xg7plugins.xg7menus.api.menus;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface MenuListener {
    void onOpen(Player player);
    void onClose(Player player);
    static MenuListener create(Consumer<Player> open, Consumer<Player> close) {
        return new MenuListener() {
            @Override
            public void onOpen(Player player) {
                open.accept(player);
            }
            @Override
            public void onClose(Player player) {
                close.accept(player);
            }
        };
    }
}
