package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.items.Item;
import com.xg7plugins.xg7menus.api.utils.Text;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemPages extends Menu {

    private List<ItemStack> pageItems;
    private Map<UUID, Integer> pageindex;
    private InventoryCoordinate coordinate1;
    private InventoryCoordinate coordinate2;
    private final int area;

    public ItemPages(String id, String title, int size, List<ItemStack> items, InventoryCoordinate inicialItemPos, InventoryCoordinate finalItemPos) {
        super(id, size, title);
        this.pageItems = items;
        this.coordinate1 = inicialItemPos;
        this.coordinate2 = finalItemPos;
        this.pageindex = new HashMap<>();
        this.area = (coordinate2.getY() - coordinate1.getY() + 1) * (coordinate2.getX() - coordinate1.getX() + 1);
    }

    public static ItemPages newMenu(String id, String title, int size, List<ItemStack> items, InventoryCoordinate inicialItemPos, InventoryCoordinate finalItemPos) {
        return new ItemPages(id, title, size, items, inicialItemPos, finalItemPos);
    }

    public ItemPages(String id, String title, int size, List<Item> invItems, List<ItemStack> items, InventoryCoordinate inicialItemPos, InventoryCoordinate finalItemPos) {
        super(id, size, title, invItems);
        this.pageItems = items;
        this.coordinate1 = inicialItemPos;
        this.coordinate2 = finalItemPos;
        this.pageindex = new HashMap<>();
        this.area = (coordinate2.getY() - coordinate1.getY() + 1) * (coordinate2.getX() - coordinate1.getX() + 1);
    }

    @NotNull
    @Override
    public Inventory getInventory() {

        Inventory inventory = Bukkit.createInventory(this, this.getSize(), Text.translateColorCodes(this.getTitle()));

        this.getItems().forEach(item -> inventory.setItem(item.getSlot(), item.getItemStack()));

        int index = 0;
        loop:
        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                if (index == pageItems.size()) break loop;
                inventory.setItem(InventoryCoordinate.toSlot(x,y),pageItems.get(index));
                index++;
            }
        }

        return inventory;
    }
    public Inventory getInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(this, this.getSize(), Text.translateColorCodes(this.getTitle()));

        this.getItems().forEach(item -> inventory.setItem(item.getSlot(), item.setPlaceHolders(player).getItemStack()));

        int index = 0;
        loop:
        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                if (index == pageItems.size()) break loop;
                inventory.setItem(InventoryCoordinate.toSlot(x,y), Item.fromItemStack(pageItems.get(index)).setPlaceHolders(player).getItemStack());
                index++;
            }
        }

        return inventory;
    }
    @Override
    public void open(Player player) {
        pageindex.put(player.getUniqueId(), 0);
        player.openInventory(getInventory(player));
    }

    public void goPage(Player player, int pageindex) {
        if (pageindex < 0) return;

        int index = area * pageindex;


        if (index >= pageItems.size()) return;
        this.pageindex.put(player.getUniqueId(), pageindex);

        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                ItemStack stack = new ItemStack(Material.AIR);
                if (index < pageItems.size()) stack = pageItems.get(index);
                updateInventory(player, Item.fromItemStack(stack).setPlaceHolders(player).setSlot(InventoryCoordinate.toSlot(x,y)));
                index++;
            }
        }

    }

    public void nextPage(Player player) {
        goPage(player, pageindex.get(player.getUniqueId()) + 1);

    }
    public void previusPage(Player player) {
        goPage(player, pageindex.get(player.getUniqueId()) - 1);

    }
}
