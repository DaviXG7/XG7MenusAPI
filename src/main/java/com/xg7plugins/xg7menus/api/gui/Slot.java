package com.xg7plugins.xg7menus.api.gui;

import lombok.Getter;

import java.util.List;

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
public class Slot {
    private final int row;
    private final int column;
    public Slot(int row, int column) {
        if (row < 1 || row > 9 || column < 1 || column > 6) throw new MenuException("Inventory cordinate invalid!");
        this.row = row;
        this.column = column;
    }
    public static int get(int row, int column) {
        return 9 * column - (9 - row) - 1;
    }
    public int get() {
        return 9 * column - (9 - row) - 1;
    }
    public static Slot fromSlot(int slot) {
        return new Slot((int) Math.ceil((double) slot / 9), slot % 9 == 0 ? 9 : slot % 9);

    }
    public static Slot fromList(List<Integer> list) {
        return new Slot(list.get(0), list.get(1));
    }
}
