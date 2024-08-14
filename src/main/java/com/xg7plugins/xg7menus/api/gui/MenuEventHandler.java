package com.xg7plugins.xg7menus.api.gui;

import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.gui.events.MenuEvent;
import com.xg7plugins.xg7menus.api.gui.menus.Menu;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class MenuEventHandler implements Listener {

    public static void onInventoryClick(InventoryClickEvent event) {

        if (!(event.getInventory().getHolder() instanceof Menu)) return;

        Menu menu = (Menu) event.getInventory().getHolder();

        ClickEvent clickEvent = new ClickEvent(
                event.getWhoClicked(),
                ClickEvent.ClickAction.valueOf(event.getClick().name()),
                event.getSlot(),
                menu.getItems().get(event.getSlot()),
                menu,
                null
        );

        if (menu.getClicks().get(event.getSlot()) != null) {
            menu.getClicks().get(event.getSlot()).accept(clickEvent);
            event.setCancelled(clickEvent.isCancelled());
            return;
        }
        if (menu.getDefaultClick() != null) {
            menu.getDefaultClick().accept(clickEvent);
            event.setCancelled(clickEvent.isCancelled());
            return;
        }
        event.setCancelled(true);
    }

    public static void onOpen(InventoryOpenEvent event) {

        if (!(event.getInventory().getHolder() instanceof Menu)) return;

        Menu menu = (Menu) event.getInventory().getHolder();
        menu.getOpenEvent().accept(new MenuEvent((Player) event.getPlayer(), menu));
    }
    public static void onClose(InventoryCloseEvent event) {

        if (!(event.getInventory().getHolder() instanceof Menu)) return;

        Menu menu = (Menu) event.getInventory().getHolder();
        menu.getOpenEvent().accept(new MenuEvent((Player) event.getPlayer(), menu));
    }

}
