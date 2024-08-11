package com.xg7plugins.xg7menus.api.menus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.xg7plugins.xg7menus.api.items.Button;
import com.xg7plugins.xg7menus.api.items.Item;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Getter
public class Menu implements InventoryHolder {

    private String title;
    private String id;
    private Button clickListener;
    private int size;
    private List<Item> items;
    private MenuListener menuListener;
    private boolean cancelInteraction = true;
    public Menu(String id, int size, String title) {
        this.id = id;
        this.size = size;
        this.title = title;
        this.items = new ArrayList<>();
    }
    public Menu(String id, int size, String title, List<Item> items) {
        this.id = id;
        this.size = size;
        this.title = title;
        this.items = items;
    }
    public Menu setClickListener(Button button) {
        this.clickListener = button;
        return this;
    }
    public Menu setItem(Item item) {
        this.items.removeIf(item1 -> item1.getSlot() == item.getSlot());
        this.items.add(item);
        return this;
    }
    public static Menu newMenu(String id, int size, String title) {
        return new Menu(id,size,title);
    }
    public Menu setCancelInteraction(boolean cancelInteraction) {
        this.cancelInteraction = cancelInteraction;
        return this;
    }
    public Menu addItems(Item... item) {
        items.addAll(Arrays.asList(item));
        return this;
    }
    public Menu addItems(List<Item> items) {
        items.addAll(items);
        return this;
    }
    public Menu solidRectangle(InventoryCoordinate initialCoord, InventoryCoordinate finalCoord, Item item) {
        for (int y = initialCoord.getY(); y <= finalCoord.getY(); y++) for (int x = initialCoord.getX(); x <= finalCoord.getX(); x++) this.items.add(item.setSlot(InventoryCoordinate.toSlot(x,y)).clone());
        return this;
    }
    public Menu borderRectangle(InventoryCoordinate initialCoord, InventoryCoordinate finalCoord, Item item) {
        for (int x = initialCoord.getX(); x <= finalCoord.getX(); x++) {
            this.items.add(item.setSlot(InventoryCoordinate.toSlot(x, initialCoord.getY())).clone());
            this.items.add(item.setSlot(InventoryCoordinate.toSlot(x, finalCoord.getY())).clone());
        }
        for (int y = initialCoord.getY() + 1; y < finalCoord.getY();  y++) {
            this.items.add(item.setSlot(InventoryCoordinate.toSlot(initialCoord.getX(), y)).clone());
            this.items.add(item.setSlot(InventoryCoordinate.toSlot(finalCoord.getX(), y)).clone());
        }
        return this;
    }
    public Menu addOpenAndCloseListener(MenuListener listener) {
        menuListener = listener;
        return this;
    }
    public void updateInventory(Player player, Item item) {
        if (player.getOpenInventory().getTopInventory().getHolder() != this) return;
        player.getOpenInventory().getTopInventory().setItem(item.getSlot(), item.getItemStack());
    }

    public void open(Player player) {
        player.openInventory(getInventory(player));
    }
    public void close(Player player) {
        player.closeInventory();
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, size, Text.translateColorCodes(title));

        this.items.forEach(item -> inventory.setItem(item.getSlot(), item.getItemStack()));

        return inventory;
    }
    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(this, size, Text.getFormatedText(player,title));

        this.items.forEach(item -> inventory.setItem(item.getSlot(), item.setPlaceHolders(player).getItemStack()));

        return inventory;
    }
    public void renderSkulls(Player player) {
        if (player.getOpenInventory().getTopInventory().getHolder() != this) return;
        for (ItemStack item : player.getOpenInventory().getTopInventory().getContents()) {
            if (item.getItemMeta() instanceof SkullMeta) {
                SkullMeta meta = (SkullMeta) item.getItemMeta();
                if (Objects.equals(meta.getOwner(), "THIS_PLAYER")) meta.setOwner(null);
                item.setItemMeta(meta);
            }
        }
    }

    /**
     * InventoryCoordinate makes easy to map the slots of the inventory
     * <br>
     * Inventory map:
     * <br><br>
     * [x, y]
     * <br><br>
     * [1, 1], [2, 1], [3, 1], [4, 1], [5, 1], [6, 1], [7, 1], [8, 1], [9, 1] <br>
     * [1, 2], [2, 2], [3, 2], [4, 2], [5, 2], [6, 2], [7, 2], [8, 2], [9, 2] <br>
     * [1, 3], [2, 3], [3, 3], [4, 3], [5, 3], [6, 3], [7, 3], [8, 3], [9, 3] <br>
     * [1, 4], [2, 4], [3, 4], [4, 4], [5, 4], [6, 4], [7, 4], [8, 4], [9, 4] <br>
     * [1, 5], [2, 5], [3, 5], [4, 5], [5, 5], [6, 5], [7, 5], [8, 5], [9, 5] <br>
     * [1, 6], [2, 6], [3, 6], [4, 6], [5, 6], [6, 6], [7, 6], [8, 6], [9, 6] <br>
     */
    @Getter
    public static class InventoryCoordinate {
        private final int x;
        private final int y;
        public InventoryCoordinate(int x, int y) {
            if (x < 1 || x > 9 || y < 1 || y > 6) throw new IllegalArgumentException("Inventory cordinate invalid!");
            this.x = x;
            this.y = y;
        }
        public static int toSlot(int x, int y) {
            return 9 * y - (9 - x) - 1;
        }
        public int toSlot() {
            return 9 * y - (9 - x) - 1;
        }
        public static Menu.InventoryCoordinate fromList(List<Integer> list) {
            return new Menu.InventoryCoordinate(list.get(0), list.get(1));
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemAdapter()).create();
        return gson.toJson(this);
    }
    public static Menu fromString(String json) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Item.class, new ItemAdapter()).create();
        return gson.fromJson(json, Menu.class);
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
