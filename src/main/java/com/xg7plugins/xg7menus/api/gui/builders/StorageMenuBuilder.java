package com.xg7plugins.xg7menus.api.gui.builders;

import com.xg7plugins.xg7menus.api.gui.MenuException;
import com.xg7plugins.xg7menus.api.gui.Slot;
import com.xg7plugins.xg7menus.api.gui.menus.StorageMenu;

public class StorageMenuBuilder extends MenuBuilder {

    private Slot minSlot;
    private Slot maxSlot;

    public StorageMenuBuilder setStorageArea(Slot slot1, Slot slot2) {
        int minRow = Math.min(slot1.getRow(), slot2.getRow());
        int minColumn = Math.min(slot1.getColumn(), slot2.getColumn());
        int maxRow = Math.max(slot1.getRow(), slot2.getRow());
        int maxColumn = Math.max(slot1.getColumn(), slot2.getColumn());
        this.minSlot = new Slot(minRow, minColumn);
        this.maxSlot = new Slot(maxRow,maxColumn);
        return this;
    }

    @Override
    public StorageMenu build() {
        if (minSlot == null || maxSlot == null) throw new MenuException("The storage inventory must have a storage area");
        if (this.title == null) throw new MenuException("The inventory must have a title!");
        if (this.type == null) {
            if (size == 0) throw new MenuException("The inventory size cannot be 0!");
            return new StorageMenu(title, size, items, clicks, defaultClick,openEvent,closeEvent,minSlot,maxSlot);
        }
        return new StorageMenu(title, type, items, clicks, defaultClick,openEvent,closeEvent,minSlot,maxSlot);
    }

}
