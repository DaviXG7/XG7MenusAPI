package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import com.xg7plugins.xg7menus.api.utils.Log;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Menu {

    @Getter
    private String id;
    @Getter
    private List<InventoryItem> items;
    private Inventory inventory;

    private String title;
    private int size;

    public Menu(String id, String title, int size) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.items = new ArrayList<>();
        Log.fine("Menu with id " + id + " has been created");
    }

    public void addItems(InventoryItem... items) {
        Collections.addAll(this.items, items);
    }
    public void updateInventory(InventoryItem item) {
        inventory.setItem(item.getSlot(), item.getItemStack());
    }
    public InventoryItem getItemBySlot(int slot) {
        return items.stream().filter(item -> item.getSlot() == slot).findFirst().orElse(null);
    }

    public void solidRectangle(InventoryCoordinate initialCoord, InventoryCoordinate finalCoord, InventoryItem item) {

        for (int y = initialCoord.getY() - 1; y < finalCoord.getX(); y++) for (int x = initialCoord.getX() - 1; x < finalCoord.getX(); x++) this.inventory.setItem(InventoryCoordinate.toSlot(x, y), item.getItemStack());
        this.items.add(item);
    }

    public void borderRectangle(InventoryCoordinate initialCoord, InventoryCoordinate finalCoord, InventoryItem item) {
        for (int x = initialCoord.getX() - 1; x < finalCoord.getX(); x++) {
            this.inventory.setItem(InventoryCoordinate.toSlot(x, initialCoord.getY()), item.getItemStack());
            this.inventory.setItem(InventoryCoordinate.toSlot(x, finalCoord.getY()), item.getItemStack());
        }
        for (int y = initialCoord.getY() - 2; y < finalCoord.getY() - 1;  y++) {
            this.inventory.setItem(InventoryCoordinate.toSlot(initialCoord.getX(), y), item.getItemStack());
            this.inventory.setItem(InventoryCoordinate.toSlot(finalCoord.getX(), y), item.getItemStack());
        }
        this.items.add(item);
    }

    public void open(Player player) {

        this.title = Text.setPlaceholders(title, player);

        inventory = Bukkit.createInventory(player, size, title);

        for (InventoryItem item : items) {
            item.setPlaceholders(player);
            inventory.setItem(item.getSlot(), item.getItemStack());
        }
        player.closeInventory();
        MenuManager.put(player, this);
        player.openInventory(inventory);
    }

    public void close(Player player) {
        player.closeInventory();
    }

    /**
     * @apiNote InventoryCoordinate
     * <br>
     * [x, y]
     * <br>
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
            if (x < 0 || x > 9 || y < 0 || y > 6) throw new IllegalArgumentException("Inventory cordinate invalid!");
            this.x = x;
            this.y = y;
        }
        public static int toSlot(int x, int y) {
            return 9 * y - (9 - x) - 1;
        }
        public int toSlot() {
            return 9 * y - (9 - x) - 1;
        }
    }






}
