package com.xg7plugins.xg7menus.api.menus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xg7plugins.xg7menus.api.MenuEventHandler;
import com.xg7plugins.xg7menus.api.items.Button;
import com.xg7plugins.xg7menus.api.items.Item;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.entity.Player;

import java.util.*;

@Getter
public class PlayerMenu {
    private List<Item> items;
    private Button clickListener;
    private MenuListener menuListener;
    private boolean cancelInteraction = true;
    public PlayerMenu() {
        this.items = new ArrayList<>();
    }
    public static PlayerMenu newMenu() {
        return new PlayerMenu();
    }
    public PlayerMenu(List<Item> items) {
        this.items = items;
    }
    public PlayerMenu setClickListener(Button button) {
        this.clickListener = button;
        return this;
    }
    public PlayerMenu setCancelInteraction(boolean cancelInteraction) {
        this.cancelInteraction = cancelInteraction;
        return this;
    }

    public PlayerMenu addItems(Item... items) {
        Collections.addAll(this.items, items);
        return this;
    }
    public PlayerMenu solidRectangle(Menu.InventoryCoordinate initialCoord, Menu.InventoryCoordinate finalCoord, Item item) {
        for (int y = initialCoord.getY(); y <= finalCoord.getY(); y++) for (int x = initialCoord.getX(); x <= finalCoord.getX(); x++) this.items.add(item.setSlot(Menu.InventoryCoordinate.toSlot(x,y)).clone());
        return this;
    }
    public PlayerMenu borderRectangle(Menu.InventoryCoordinate initialCoord, Menu.InventoryCoordinate finalCoord, Item item) {
        for (int x = initialCoord.getX(); x <= finalCoord.getX(); x++) {
            this.items.add(item.setSlot(Menu.InventoryCoordinate.toSlot(x, initialCoord.getY())).clone());
            this.items.add(item.setSlot(Menu.InventoryCoordinate.toSlot(x, finalCoord.getY())).clone());
        }
        for (int y = initialCoord.getY() + 1; y < finalCoord.getY();  y++) {
            this.items.add(item.setSlot(Menu.InventoryCoordinate.toSlot(initialCoord.getX(), y)).clone());
            this.items.add(item.setSlot(Menu.InventoryCoordinate.toSlot(finalCoord.getX(), y)).clone());
        }
        return this;
    }
    public PlayerMenu addOpenAndCloseListener(MenuListener listener) {
        menuListener = listener;
        return this;
    }
    public void updateInventory(Player player, Item builder) {
        player.getInventory().setItem(builder.getSlot(), builder.getItemStack());
    }
    public void open(Player player) {
        MenuEventHandler.register(player, this);
        player.getInventory().clear();
        items.forEach(item -> player.getInventory().setItem(item.getSlot(), item.getItemStack()));
        if (menuListener == null) return;
        menuListener.onOpen(player);
    }
    public void close(Player player) {
        player.getInventory().clear();
        MenuEventHandler.getOlditems().get(player.getUniqueId()).forEach((key, value) -> player.getInventory().setItem(key, value));
        MenuEventHandler.unregister(player);
        if (menuListener == null) return;
        menuListener.onClose(player);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemAdapter()).create();
        return gson.toJson(this);
    }
    public static PlayerMenu fromString(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemAdapter()).create();
        return gson.fromJson(json, PlayerMenu.class);
    }

    private static class ItemAdapter extends TypeAdapter<Item> {

        @SneakyThrows
        @Override
        public void write(JsonWriter out, Item value) {
            out.value(value.toString());
        }

        @SneakyThrows
        @Override
        public Item read(JsonReader in)  {
            return Item.fromString(in.nextString());
        }
    }
}
