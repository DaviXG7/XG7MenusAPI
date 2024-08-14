package com.xg7plugins.xg7menus.api.gui.menus;

import com.xg7plugins.xg7menus.api.gui.MenuItem;
import com.xg7plugins.xg7menus.api.gui.builders.ItemBuilder;
import com.xg7plugins.xg7menus.api.gui.builders.MenuBuilder;
import com.xg7plugins.xg7menus.api.gui.builders.PageMenuBuilder;
import com.xg7plugins.xg7menus.api.gui.builders.StorageMenuBuilder;
import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.gui.events.MenuEvent;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.function.Consumer;

@Getter
public class Menu implements InventoryHolder {

    private Inventory inventory;
    private String title;
    private InventoryType inventoryType;
    private HashMap<Integer, MenuItem> items = new HashMap<>();
    private HashMap<Integer, Consumer<ClickEvent>> clicks = new HashMap<>();
    private Consumer<ClickEvent> defaultClick;
    private Consumer<MenuEvent> openEvent;
    private Consumer<MenuEvent> closeEvent;

    public Menu(String title, int size) {
        this.title = title;
        inventory = Bukkit.createInventory(this, size, title);
    }
    public Menu(String title, InventoryType type) {
        this.title = title;
        inventory = Bukkit.createInventory(this, type, title);
        this.inventoryType = type;
    }
    public Menu(String title, int size, HashMap<Integer, MenuItem> items, HashMap<Integer, Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent) {
        this.title = title;
        inventory = Bukkit.createInventory(this, size, title);
        this.items = items;
        this.clicks = clicks;
        this.defaultClick = defaultClick;
        this.openEvent = openEvent;
        this.closeEvent = closeEvent;
    }
    public Menu(String title, InventoryType type, HashMap<Integer, MenuItem> items, HashMap<Integer, Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent) {

        inventory = Bukkit.createInventory(this, type, title);
        this.items = items;
        this.clicks = clicks;
        this.defaultClick = defaultClick;
        this.openEvent = openEvent;
        this.closeEvent = closeEvent;
        this.inventoryType = type;
        this.title = title;

    }
    @Contract(" -> new")
    public static @NotNull MenuBuilder gui() {
        return new MenuBuilder();
    }
    @Contract(" -> new")
    public static @NotNull StorageMenuBuilder storage() {
        return new StorageMenuBuilder();
    }
    @Contract(" -> new")
    public static @NotNull PageMenuBuilder pages() {
        return new PageMenuBuilder();
    }
    public UpdatingView updateAll() {
        return new UpdatingView();
    }
    public UpdatingView update(Player player) {
        return new UpdatingView(player);
    }
    public void open(@NotNull Player player) {
        setPlaceholders(player);
    }

    public void close(@NotNull Player player) {
        player.closeInventory();
    }
    public void setItem(int slot, MenuItem item) {
        items.put(slot,item);
    }
    public void setItem(int slot, MenuItem item, Consumer<ClickEvent> newAction) {
        items.put(slot,item);
        clicks.put(slot,newAction);
    }

    public void setPlaceholders(Player player) {
        Menu menu = inventoryType == null ? new Menu(Text.format(title).setPlaceholders(player).getText(), this.inventory.getSize(), new HashMap<>(), clicks, defaultClick, openEvent, closeEvent) : new Menu(Text.format(title).setPlaceholders(player).getText(), inventoryType, new HashMap<>(), clicks, defaultClick, openEvent, closeEvent);

        items.forEach((key, value) -> menu.setItem(key, ItemBuilder.from(value).setPlaceHolders(player).asMenuItem()));

        menu.open(player);
    }

    public class UpdatingView {

        private final Player player;

        public UpdatingView() {
            player = null;
        }

        public UpdatingView(Player player) {
            this.player = player;
        }
        public UpdatingView updateItem(int slot, MenuItem item) {
            if (player != null) {
                player.getOpenInventory().getTopInventory().setItem(slot, item.getItemStack());
                return this;
            }
            inventory.setItem(slot, item.getItemStack());
            return this;
        }
        public UpdatingView updateItem(int slot, MenuItem item, Consumer<ClickEvent> newAction) {
            if (player != null) {
                player.getOpenInventory().getTopInventory().setItem(slot, item.getItemStack());
                return this;
            }
            if (newAction != null) clicks.put(slot, newAction);
            inventory.setItem(slot, item.getItemStack());
            return this;
        }

    }
    @NotNull
    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}
