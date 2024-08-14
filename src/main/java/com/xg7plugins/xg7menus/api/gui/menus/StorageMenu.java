package com.xg7plugins.xg7menus.api.gui.menus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xg7plugins.xg7menus.api.gui.MenuItem;
import com.xg7plugins.xg7menus.api.gui.Slot;
import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.gui.events.MenuEvent;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.function.Consumer;

@Getter
public class StorageMenu extends Menu {
    private HashMap<Integer, ItemStack> storedItems = new HashMap<>();
    private Slot minSlot;
    private Slot maxSlot;

    public StorageMenu(String title, int size, Slot minSlot, Slot maxSlot) {
        super(title, size);
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    public StorageMenu(String title, InventoryType type, Slot minSlot, Slot maxSlot) {
        super(title, type);
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    public StorageMenu(String title, int size, HashMap<Integer, MenuItem> items, HashMap<Integer, Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent, Slot minSlot, Slot maxSlot) {
        super(title, size, items, clicks, defaultClick, openEvent, closeEvent);
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    public StorageMenu(String title, InventoryType type, HashMap<Integer, MenuItem> items, HashMap<Integer, Consumer<ClickEvent>> clicks, Consumer<ClickEvent> defaultClick, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent, Slot minSlot, Slot maxSlot) {
        super(title, type, items, clicks, defaultClick, openEvent, closeEvent);
        this.minSlot = minSlot;
        this.maxSlot = maxSlot;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeAdapter(MenuItem.class, new ItemAdapter()).create();
        return gson.toJson(this);
    }
    public static StorageMenu fromString(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(MenuItem.class, new ItemAdapter()).create();
        return gson.fromJson(json, StorageMenu.class);
    }

    private static class ItemAdapter extends TypeAdapter<MenuItem> {

        @SneakyThrows
        @Override
        public void write(JsonWriter out, MenuItem value) {
            out.value(value.toString());
        }

        @SneakyThrows
        @Override
        public MenuItem read(JsonReader in)  {
            return MenuItem.fromString(in.nextString());
        }
    }
}
