package com.xg7plugins.xg7menus.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class StorageMenuOpenEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();
    private Inventory inventory;

    public StorageMenuOpenEvent(Inventory inventory, Player player) {
        super(player);
        this.inventory = inventory;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
