package com.xg7plugins.xg7menus.api.events;

import com.xg7plugins.xg7menus.api.menus.StorageMenu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;
@Getter
public class StorageMenuCloseEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private StorageMenu menu;

    public StorageMenuCloseEvent(StorageMenu menu, Player player) {
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
