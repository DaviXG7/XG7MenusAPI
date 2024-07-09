package com.xg7plugins.xg7menus.api.menus.supers;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Arrays;
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
    }

    public void addItems(InventoryItem... items) {
        this.items = Arrays.asList(items);
    }
    public void updateInventory(InventoryItem item) {
        if (inventory.getHolder() instanceof Player) {
            Player owner = (Player) inventory.getHolder();
            inventory.setItem(item.getSlot(), item.getItemStack());
        }
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
        player.openInventory(inventory);
        MenuManager.put(player, this);
    }






}
