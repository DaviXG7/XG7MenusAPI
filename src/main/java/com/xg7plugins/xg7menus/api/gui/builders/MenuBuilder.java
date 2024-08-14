package com.xg7plugins.xg7menus.api.gui.builders;

import com.xg7plugins.xg7menus.api.gui.Slot;
import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.gui.menus.Menu;
import com.xg7plugins.xg7menus.api.gui.MenuException;
import com.xg7plugins.xg7menus.api.gui.MenuItem;
import com.xg7plugins.xg7menus.api.gui.events.MenuEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.function.Consumer;

public class MenuBuilder {
    protected Inventory inventory;
    protected String title;
    protected int size;
    protected final HashMap<Integer, MenuItem> items = new HashMap<>();
    protected final HashMap<Integer, Consumer<ClickEvent>> clicks = new HashMap<>();
    protected Consumer<ClickEvent> defaultClick = clickEvent -> clickEvent.setCancelled(false);
    protected Consumer<MenuEvent> openEvent;
    protected Consumer<MenuEvent> closeEvent;
    protected InventoryType type;

    public MenuBuilder title(String text) {
        this.title = text;
        return this;
    }
    public MenuBuilder size(int size) {
        this.size = size;
        return this;
    }
    public MenuBuilder rows(int rows) {
        this.size = rows * 9;
        return this;
    }
    public MenuBuilder type(InventoryType type) {
        this.type = type;
        return this;
    }
    public MenuBuilder setItem(ItemBuilder builder, int slot) {
        items.put(slot, builder.asMenuItem());
        if (builder.getEvent() != null) clicks.put(slot, builder.getEvent());
        return this;
    }
    public MenuBuilder openEvent(Consumer<MenuEvent> event) {
        this.openEvent = event;
        return this;
    }
    public MenuBuilder closeEvent(Consumer<MenuEvent> event) {
        this.closeEvent = event;
        return this;
    }
    public MenuBuilder setDefaultClick(Consumer<ClickEvent> click) {
        this.defaultClick = click;
        return this;
    }
    public MenuBuilder setSlotClickAction(Consumer<ClickEvent> click, int slot) {
        clicks.put(slot, click);
        return this;
    }
    public MenuBuilder solidRectangle(Slot initialCoord, Slot finalCoord, ItemBuilder item) {
        for (int y = initialCoord.getRow(); y <= finalCoord.getColumn(); y++) for (int x = initialCoord.getRow(); x <= finalCoord.getRow(); x++) setItem(item, Slot.get(x,y));
        return this;
    }
    public MenuBuilder borderRectangle(Slot initialCoord, Slot finalCoord, ItemBuilder item) {
        for (int x = initialCoord.getRow(); x <= finalCoord.getRow(); x++) {
            setItem(item, Slot.get(x,initialCoord.getColumn()));
            setItem(item, Slot.get(x,finalCoord.getColumn()));
        }
        for (int y = initialCoord.getColumn() + 1; y < finalCoord.getColumn();  y++) {
            setItem(item, Slot.get(initialCoord.getRow(),y));
            setItem(item, Slot.get(finalCoord.getRow(),y));
        }
        return this;
    }
    public Menu build() {
        if (title == null) throw new MenuException("The inventory must have a title!");
        if (type == null) {
            if (size == 0) throw new MenuException("The inventory size cannot be 0!");
            return new Menu(title, size, items, clicks, defaultClick,openEvent,closeEvent);
        }
        return new Menu(title, type, items, clicks, defaultClick,openEvent,closeEvent);
    }
}
