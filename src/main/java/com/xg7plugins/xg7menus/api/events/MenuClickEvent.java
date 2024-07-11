package com.xg7plugins.xg7menus.api.events;

import com.xg7plugins.xg7menus.api.menus.InventoryItem;
import com.xg7plugins.xg7menus.api.menus.Menu;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

@Getter
public class MenuClickEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    private Menu menu;
    private InventoryItem inventoryItem;
    private MenuClickType clickType;
    private Location location;
    private int slot;

    /**
     * The constuctor of the event
     * @param menu The menu of this event
     * @param inventoryItem The inventoryItem used on this event
     * @param clickType The click type of this event. Can be the click types of InventoryClickEvent or PlayerInteractEvent
     * @param location The location of click (If this menu is a PlayerMenu)
     * @param slot The slot of this event
     * @param player The player who called this event
     * @see MenuClickType
     * @see org.bukkit.event.block.Action
     * @see org.bukkit.event.inventory.ClickType
     * @see com.xg7plugins.xg7menus.api.menus.PlayerMenu
     * @see Menu
     */
    public MenuClickEvent(Menu menu, InventoryItem inventoryItem, MenuClickType clickType, Location location, int slot, Player player) {
        super(player);
        this.menu = menu;
        this.inventoryItem = inventoryItem;
        this.clickType = clickType;
        this.location = location;
        this.slot = slot;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
