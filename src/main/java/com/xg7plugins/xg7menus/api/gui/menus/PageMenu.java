package com.xg7plugins.xg7menus.api.gui.menus;


import com.xg7plugins.xg7menus.api.gui.MenuException;
import com.xg7plugins.xg7menus.api.gui.MenuItem;
import com.xg7plugins.xg7menus.api.gui.Slot;
import com.xg7plugins.xg7menus.api.gui.builders.ItemBuilder;
import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.gui.events.MenuEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.*;
import java.util.function.Consumer;

public class PageMenu extends Menu {

    private Slot initSlot;
    private Slot finalSlot;
    private final Map<UUID, Integer> pageindex = new HashMap<>();
    private List<ItemBuilder> items;
    private int area;

    public PageMenu(String title, int size, Slot initSlot, Slot finalSlot, List<ItemBuilder> items) {
        super(title, size);
        this.initSlot = initSlot;
        this.finalSlot = finalSlot;
        this.items = items;
        this.area = (finalSlot.getRow() - initSlot.getRow()) * (finalSlot.getColumn() - initSlot.getColumn());
    }

    public PageMenu(String title, InventoryType type,Slot initSlot, Slot finalSlot, List<ItemBuilder> items) {
        super(title, type);
        this.initSlot = initSlot;
        this.finalSlot = finalSlot;
        this.items = items;
    }

    public PageMenu(String title, int size, HashMap<Integer, MenuItem> mappedItems, HashMap<Integer, Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent,Slot initSlot, Slot finalSlot, List<ItemBuilder> items) {
        super(title, size, mappedItems, clicks, defaultClick, openEvent, closeEvent);
        this.initSlot = initSlot;
        this.finalSlot = finalSlot;
        this.items = items;
    }

    public PageMenu(String title, InventoryType type, HashMap<Integer, MenuItem> mappedItems, HashMap<Integer, Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent,Slot initSlot, Slot finalSlot, List<ItemBuilder> items) {
        super(title, type, mappedItems, clicks, defaultClick, openEvent, closeEvent);
        this.initSlot = initSlot;
        this.finalSlot = finalSlot;
        this.items = items;
    }


    public void goPage(int index, Player player) {
        if (index * area >= items.size()) throw new MenuException("Index out of bounds");
        if (index < 0) throw new MenuException("Index out of bounds");

        this.pageindex.put(player.getUniqueId(), index);

        int indexator = index * area;

        for (int y = initSlot.getColumn(); y <= finalSlot.getColumn(); y++) {
            for (int x = initSlot.getRow(); x <= finalSlot.getRow(); x++) {
                if (indexator >= items.size()) break;
                super.update(player).updateItem(Slot.get(x, y), items.get(indexator).asMenuItem(), items.get(indexator).getEvent());
                indexator++;
            }
        }
    }
    public void nextPage(Player player) {
        if ((pageindex.get(player.getUniqueId())+ 1) * area >= items.size()) return;
        goPage(pageindex.get(player.getUniqueId()) + 1, player);
    }
    public void previousPage(Player player) {
        if (pageindex.get(player.getUniqueId()) - 1 < 0) return;
        goPage(pageindex.get(player.getUniqueId()) - 1, player);
    }
}
