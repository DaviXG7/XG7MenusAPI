package com.xg7plugins.xg7menus.api.events;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import com.xg7plugins.xg7menus.api.manager.StorageMenuManager;
import com.xg7plugins.xg7menus.api.menus.Menu;
import com.xg7plugins.xg7menus.api.menus.StorageMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuEventHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Menu menu = MenuManager.getMenuByPlayer((Player) event.getWhoClicked());
        if (menu == null) return;
        event.setCancelled(true);

        Bukkit.getPluginManager().callEvent(new MenuClickEvent(menu, menu.getItemBySlot(event.getRawSlot()), MenuClickType.valueOf(event.getClick().name()), null, event.getRawSlot(), (Player) event.getWhoClicked()));

    }
    @EventHandler
    public void onMenuOpen(InventoryOpenEvent event) {
        Menu menu = MenuManager.getMenuByPlayer((Player) event.getPlayer());
        if (menu == null) return;
        Bukkit.getPluginManager().callEvent(new MenuOpenEvent(menu, (Player) event.getPlayer()));
    }
    @EventHandler
    public void onMenuClose(InventoryCloseEvent event) {
        Menu menu = MenuManager.getMenuByPlayer((Player) event.getPlayer());
        if (menu == null) return;
        MenuManager.remove((Player) event.getPlayer());
        Bukkit.getPluginManager().callEvent(new MenuCloseEvent(menu, (Player) event.getPlayer()));
    }
    @EventHandler
    public void onStorageMenuOpen(InventoryOpenEvent event) {
        if (!StorageMenuManager.getUsingStorageInventory().contains(event.getPlayer().getUniqueId())) return;
        Bukkit.getPluginManager().callEvent(new StorageMenuOpenEvent(event.getInventory(), (Player) event.getPlayer()));
    }
    @EventHandler
    public void onStorageClose(InventoryCloseEvent event) {
        if (!StorageMenuManager.getUsingStorageInventory().contains(event.getPlayer().getUniqueId())) return;
        StorageMenuManager.remove((Player) event.getPlayer());
        Bukkit.getPluginManager().callEvent(new StorageMenuCloseEvent(new StorageMenu(event.getInventory()), (Player) event.getPlayer()));
    }

}
