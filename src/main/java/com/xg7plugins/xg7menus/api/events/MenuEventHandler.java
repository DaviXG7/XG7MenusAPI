package com.xg7plugins.xg7menus.api.events;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import com.xg7plugins.xg7menus.api.manager.StorageMenuManager;
import com.xg7plugins.xg7menus.api.menus.Menu;
import com.xg7plugins.xg7menus.api.menus.PlayerMenu;
import com.xg7plugins.xg7menus.api.menus.StorageMenu;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class MenuEventHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Menu menu = MenuManager.getMenuByPlayer((Player) event.getWhoClicked());
        if (menu == null) return;
        event.setCancelled(true);

        Bukkit.getPluginManager().callEvent(new MenuClickEvent(menu, menu.getItemBySlot(event.getRawSlot()), MenuClickType.valueOf(event.getClick().name()), null, event.getRawSlot(), (Player) event.getWhoClicked()));

    }
    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        Menu menu = MenuManager.getMenuByPlayer(event.getPlayer());
        if (menu == null) return;

        event.setCancelled(!((PlayerMenu)menu).isCanInteract());

        Bukkit.getPluginManager().callEvent(new MenuClickEvent(menu, menu.getItemBySlot(event.getPlayer().getInventory().getHeldItemSlot()), MenuClickType.valueOf(event.getAction().name()), event.getClickedBlock() == null ? null : event.getClickedBlock().getLocation(), event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer()));

    }
    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        Menu menu = MenuManager.getMenuByPlayer(event.getPlayer());
        if (menu == null) return;

        event.setCancelled(!((PlayerMenu)menu).isCanBreak());
    }
    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent event) {
        Menu menu = MenuManager.getMenuByPlayer(event.getPlayer());
        if (menu == null) return;

        event.setCancelled(!((PlayerMenu)menu).isCanBuild());
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
