package com.xg7plugins.xg7menus.api.menus.gui;

import com.xg7plugins.xg7menus.api.menus.BaseMenu;
import com.xg7plugins.xg7menus.api.menus.MenuPermissions;
import com.xg7plugins.xg7menus.api.menus.builders.item.ItemBuilder;
import com.xg7plugins.xg7menus.api.menus.events.ClickEvent;
import com.xg7plugins.xg7menus.api.menus.events.MenuEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public class Menu extends BaseMenu implements InventoryHolder {

    protected Inventory inventory;

    public Menu(String title, int size, Map<Integer, ItemStack> items, Map<Integer,Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent, EnumSet<MenuPermissions> permissions, HumanEntity player) {
        super(defaultClick,openEvent,closeEvent,items,clicks,permissions,player);
        this.inventory = Bukkit.createInventory(this, size, title);
        update();
    }
    public Menu(String title, InventoryType type, Map<Integer, ItemStack> items, Map<Integer,Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent, EnumSet<MenuPermissions> permissions, HumanEntity player) {
        super(defaultClick, openEvent, closeEvent, items, clicks, permissions, player);
        this.inventory = Bukkit.createInventory(this, type, title);
        update();
    }

    public void setItem(int slot, ItemStack item) {
        items.put(slot, item);
    }
    public void setItem(int slot, ItemStack item, Consumer<ClickEvent> clickEvent) {
        items.put(slot, item);
        clickEvents.put(slot, clickEvent);
    }
    public void setClickEvent(int slot, Consumer<ClickEvent> clickEvent) {
        clickEvents.put(slot, clickEvent);
    }

    public void update() {
        items.forEach((key, value) -> inventory.setItem(key, value));
    }

    public void open() {
        player.openInventory(inventory);
    }
    public void close() {
        player.closeInventory();
    }

    public void updateItem(int slot, ItemBuilder builder) {
        inventory.setItem(slot, builder.toItemStack());
        items.put(slot, builder.toItemStack());
        if (builder.getEvent() != null) clickEvents.put(slot, builder.getEvent());
        else clickEvents.remove(slot);
    }
}
