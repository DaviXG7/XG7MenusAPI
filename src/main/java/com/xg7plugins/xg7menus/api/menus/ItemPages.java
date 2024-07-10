package com.xg7plugins.xg7menus.api.menus;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@Getter
public class ItemPages extends Menu {

    private List<InventoryItem> pageItems;
    private int pageindex;
    private InventoryCoordinate coordinate1;
    private InventoryCoordinate coordinate2;
    private final int area;


    public ItemPages(String id, String title, int size, List<InventoryItem> items, InventoryCoordinate inicialItemPos, InventoryCoordinate finalItemPos) {
        super(id, title, size);
        this.pageItems = items;
        this.coordinate1 = inicialItemPos;
        this.coordinate2 = finalItemPos;
        this.area = (coordinate2.getY() - coordinate1.getY() + 1) * (coordinate2.getX() - coordinate1.getX() + 1);
    }

    @Override
    public void open(Player player) {

        int index = 0;
        this.pageindex = 0;
        loop:
        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                if (index == pageItems.size()) break loop;
                InventoryItem inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                addItems(inventoryItem);
                index++;
            }
        }

        super.open(player);
    }

    public void goPage(int pageindex) {
        this.pageindex = pageindex;

        int index = area * pageindex;

        if (index >= pageItems.size()) return;

        List<InventoryItem> items = pageItems.subList(index, pageItems.size());

        loop:
        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                if (index == pageItems.size()) break loop;
                InventoryItem inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                updateInventory(inventoryItem);
                index++;

            }
        }

    }

    public void nextPage() {

        int index = area * (pageindex + 1);

        if (index >= pageItems.size()) return;
        pageindex++;

        List<InventoryItem> items = pageItems.subList(index, pageItems.size());


        loop:
        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                InventoryItem inventoryItem = new InventoryItem(new ItemStack(Material.AIR), -1);
                if (index < pageItems.size()) inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                updateInventory(inventoryItem);
                index++;

            }
        }

    }
    public void previusPage() {
        if (pageindex -1 < 0) return;
        pageindex--;

        int index = area * pageindex;

        if (index >= pageItems.size()) return;

        List<InventoryItem> items = pageItems.subList(index, pageItems.size());

        loop:
        for (int y = coordinate1.getY(); y <= coordinate2.getY(); y++) {
            for (int x = coordinate1.getX(); x <= coordinate2.getX(); x++) {
                if (index == pageItems.size()) break loop;
                InventoryItem inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                updateInventory(inventoryItem);
                index++;

            }
        }

    }
}
