package com.xg7plugins.xg7menus.api.menus;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.List;

@Getter
public class ItemPages extends Menu {

    private List<InventoryItem> pageItems;
    private int pageindex;
    private InventoryCoordinate coordinate1;
    private InventoryCoordinate coordinate2;


    public ItemPages(String id, String title, int size, List<InventoryItem> items, InventoryCoordinate inicialItemPos, InventoryCoordinate finalItemPos) {
        super(id, title, size);
        this.pageItems = items;
        this.coordinate1 = inicialItemPos;
        this.coordinate2 = finalItemPos;
    }

    @Override
    public void open(Player player) {

        int index = 0;
        this.pageindex = 0;
        for (int y = coordinate1.getY(); y < coordinate2.getY(); y++) {
            if (pageItems.get(index) == null) break;
            for (int x = coordinate1.getX(); x < coordinate2.getY(); x++) {
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

        int altura = coordinate2.getY() - coordinate1.getY() + 1;
        int largura = coordinate2.getX() - coordinate1.getX() + 1;

        int index = altura * largura * pageindex;

        if (pageItems.get(index) == null) return;

        List<InventoryItem> items = pageItems.subList(index, pageItems.size());


        for (int y = coordinate1.getY(); y < coordinate2.getY(); y++) {
            if (pageItems.get(index) == null) break;
            for (int x = coordinate1.getX(); x < coordinate2.getY(); x++) {
                InventoryItem inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                updateInventory(inventoryItem);
                index++;
            }
        }

    }

    public void nextPage() {
        int altura = coordinate2.getY() - coordinate1.getY() + 1;
        int largura = coordinate2.getX() - coordinate1.getX() + 1;

        int index = altura * largura * (pageindex + 1);

        if (pageItems.get(index) == null) return;
        pageindex++;

        List<InventoryItem> items = pageItems.subList(index, pageItems.size());


        for (int y = coordinate1.getY(); y < coordinate2.getY(); y++) {
            if (pageItems.get(index) == null) break;
            for (int x = coordinate1.getX(); x < coordinate2.getY(); x++) {
                InventoryItem inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                updateInventory(inventoryItem);
                index++;
            }
        }

    }
    public void previusPage() {
        if (pageindex -1 < 0) return;
        pageindex--;
        int altura = coordinate2.getY() - coordinate1.getY() + 1;
        int largura = coordinate2.getX() - coordinate1.getX() + 1;

        int index = altura * largura * pageindex;

        if (pageItems.get(index) == null) return;

        List<InventoryItem> items = pageItems.subList(index, pageItems.size());


        for (int y = coordinate1.getY(); y < coordinate2.getY(); y++) {
            if (pageItems.get(index) == null) break;
            for (int x = coordinate1.getX(); x < coordinate2.getY(); x++) {
                InventoryItem inventoryItem = pageItems.get(index);
                inventoryItem.setSlot(InventoryCoordinate.toSlot(x,y));
                updateInventory(inventoryItem);
                index++;
            }
        }

    }
}
