package com.xg7plugins.xg7menus.api.gui.builders;

import com.xg7plugins.xg7menus.api.gui.MenuException;
import com.xg7plugins.xg7menus.api.gui.Slot;
import com.xg7plugins.xg7menus.api.gui.menus.PageMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageMenuBuilder extends MenuBuilder {

    private Slot initSlot;
    private Slot finalSlot;

    private final List<ItemBuilder> listItems = new ArrayList<>();

    public PageMenuBuilder setArea(Slot initSlot, Slot finalSlot) {
        int minRow = Math.min(initSlot.getRow(), finalSlot.getRow());
        int minColumn = Math.min(initSlot.getColumn(), finalSlot.getColumn());
        int maxRow = Math.max(initSlot.getRow(), finalSlot.getRow());
        int maxColumn = Math.max(initSlot.getColumn(), finalSlot.getColumn());
        this.initSlot = new Slot(minRow, minColumn);
        this.finalSlot = new Slot(maxRow,maxColumn);
        return this;
    }
    public PageMenuBuilder addItems(ItemBuilder... builders) {
        listItems.addAll(Arrays.asList(builders));
        return this;
    }

    @Override
    public PageMenu build() {
        if (initSlot == null || finalSlot == null) throw new MenuException("The storage inventory must have a storage area");
        if (this.title == null) new MenuException("The inventory must have a title!");
        if (this.type == null) {
            if (size == 0) throw new MenuException("The inventory size cannot be 0!");
            return new PageMenu(title, size, items, clicks, defaultClick,openEvent,closeEvent,initSlot,finalSlot,listItems);
        }
        return new PageMenu(title, type, items, clicks, defaultClick,openEvent,closeEvent,initSlot,finalSlot,listItems);
    }



}
