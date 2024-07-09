package com.xg7plugins.xg7menus.api.event;

import com.xg7plugins.xg7menus.api.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public class MenuOpenEvent extends PlayerEvent {

    private static final HandlerList handlers = new HandlerList();
    private Menu menu;

    public MenuOpenEvent(Menu menu, Player player) {
        super(player);
        this.menu = menu;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
