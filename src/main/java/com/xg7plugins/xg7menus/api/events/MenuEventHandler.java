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
import org.bukkit.event.player.PlayerQuitEvent;

public class MenuEventHandler implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Menu menu = MenuManager.getMenuByPlayer((Player) event.getWhoClicked());

        if (menu == null) {
            menu = MenuManager.getPlayerMenuByPlayer((Player) event.getWhoClicked());
            if (menu == null) return;
        }
        if (!(menu instanceof PlayerMenu) && event.getRawSlot() > menu.getInventory().getSize() - 1) {
            menu = MenuManager.getPlayerMenuByPlayer((Player) event.getWhoClicked());
        }
        event.setCancelled(true);

        Bukkit.getPluginManager().callEvent(new MenuClickEvent(menu, menu.getItemBySlot(event.getSlot()), MenuClickType.valueOf(event.getClick().name()), null, event.getSlot(), (Player) event.getWhoClicked()));

    }
    @EventHandler
    public void onInteractEvent(PlayerInteractEvent event) {
        PlayerMenu menu = MenuManager.getPlayerMenuByPlayer(event.getPlayer());
        if (menu == null) return;

        event.setCancelled(!menu.isCanInteract());

        Bukkit.getPluginManager().callEvent(new MenuClickEvent(menu, menu.getItemBySlot(event.getPlayer().getInventory().getHeldItemSlot()), MenuClickType.valueOf(event.getAction().name()), event.getClickedBlock() == null ? null : event.getClickedBlock().getLocation(), event.getPlayer().getInventory().getHeldItemSlot(), event.getPlayer()));

    }
    @EventHandler
    public void onBreakEvent(BlockBreakEvent event) {
        PlayerMenu menu = MenuManager.getPlayerMenuByPlayer(event.getPlayer());
        if (menu == null) return;

        event.setCancelled(!menu.isCanBreak());
    }
    @EventHandler
    public void onPlaceEvent(BlockPlaceEvent event) {
        PlayerMenu menu = MenuManager.getPlayerMenuByPlayer(event.getPlayer());
        if (menu == null) return;

        event.setCancelled(!menu.isCanBuild());
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
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (MenuManager.getPlayerMenuByPlayer(event.getPlayer()) != null) {
            MenuManager.getPlayerMenuByPlayer(event.getPlayer());
        }

        MenuManager.removePlayerMenu(event.getPlayer());
        MenuManager.remove(event.getPlayer());
    }

}
