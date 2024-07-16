package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import com.xg7plugins.xg7menus.api.utils.Log;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

/**
 * This is the main class of a menu, where you can <br>
 * create menus in a much simpler way, add items<br>
 * easily, and update the inventory whenever you want.
 * @see MenuManager
 */
public class Menu {

    @Getter
    private String id;
    @Getter
    private List<InventoryItem> items;
    @Getter
    protected Inventory inventory;
    @Setter
    private InventoryItem fillItem = null;

    private String title;
    private int size;

    /**
     * Menu contructor
     * @param id The id of the menu
     * @param title The title of the inventory
     * @param size The size of the inventory
     */
    public Menu(String id, String title, int size) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.items = new ArrayList<>();
        Log.fine("Menu with id " + id + " has been created");
    }

    /**
     * This method add an item to the inventory
     * @param items Items that will be aded
     */
    public void addItems(InventoryItem... items) {
        Collections.addAll(this.items, items);
    }

    /**
     * This method update the list and the player <br>
     * inventory, replacing the item in inventoryItem slot <br>
     * with inventoryItem itemStack
     * @param item
     */
    public void updateInventory(InventoryItem item) {
        items.replaceAll(itemToReplace -> itemToReplace.getSlot() == itemToReplace.getSlot() ? item : itemToReplace);
        inventory.setItem(item.getSlot(), item.getItemStack());
    }

    public InventoryItem getItemBySlot(int slot) {
        return items.stream().filter(item -> item.getSlot() == slot).findFirst().orElse(null);
    }

    /**
     * Makes a solid rectangle of items in the inventory
     * @param initialCoord First coordinate of the rectangle
     * @param finalCoord Second coordinate of the rectangle
     * @param item The item that will fill the rectangle
     */
    public void solidRectangle(InventoryCoordinate initialCoord, InventoryCoordinate finalCoord, InventoryItem item) {
        for (int y = initialCoord.getY(); y <= finalCoord.getY(); y++) for (int x = initialCoord.getX(); x <= finalCoord.getX(); x++) this.addItems(new InventoryItem(item.getItemStack(), InventoryCoordinate.toSlot(x,y)));
    }

    /**
     * Makes an empty rectangle with border of items in the inventory
     * @param initialCoord First coordinate of the rectangle
     * @param finalCoord Second coordinate of the rectangle
     * @param item The item that will br the border of the rectangle
     */
    public void borderRectangle(InventoryCoordinate initialCoord, InventoryCoordinate finalCoord, InventoryItem item) {
        for (int x = initialCoord.getX(); x <= finalCoord.getX(); x++) {
            this.addItems(new InventoryItem(item.getItemStack(), InventoryCoordinate.toSlot(x, initialCoord.getY())));
            this.addItems(new InventoryItem(item.getItemStack(), InventoryCoordinate.toSlot(x, finalCoord.getY())));
        }
        for (int y = initialCoord.getY() + 1; y < finalCoord.getY();  y++) {
            this.addItems(new InventoryItem(item.getItemStack(), InventoryCoordinate.toSlot(initialCoord.getX(), y)));
            this.addItems(new InventoryItem(item.getItemStack(), InventoryCoordinate.toSlot(finalCoord.getX(), y)));
        }
    }

    /**
     * Creates the inventory with stored items <br>
     * and open to a player
     * @param player Player who will open the inventory
     */
    public void open(Player player) {

        this.title = Text.setPlaceholders(title, player);

        inventory = Bukkit.createInventory(player, size, title);
        if (fillItem != null) IntStream.range(0, inventory.getSize()).forEach(i -> inventory.setItem(i, fillItem.getItemStack()));

        for (InventoryItem item : items) {
            item.setPlaceholders(player);
            inventory.setItem(item.getSlot(), item.getItemStack());
        }
        player.closeInventory();
        MenuManager.put(player, this);
        player.openInventory(inventory);
    }

    /**
     * This method closes the player inventory
     */
    public void close(Player player) {
        player.closeInventory();
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
    }






}
