package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.menus.events.ClickEvent;
import com.xg7plugins.xg7menus.api.menus.events.MenuEvent;
import lombok.Getter;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.Map;
import java.util.function.Consumer;
@Getter
public abstract class BaseMenu {

    protected Map<Integer, ItemStack> items;
    protected Map<Integer, Consumer<ClickEvent>> clickEvents;
    protected Consumer<ClickEvent> defaultClickEvent;
    protected Consumer<MenuEvent> openEvent;
    protected Consumer<MenuEvent> closeEvent;
    protected EnumSet<MenuPermissions> permissions;
    protected HumanEntity player;

    public BaseMenu(
            Consumer<ClickEvent> defaultClickEvent,
            Consumer<MenuEvent> openEvent,
            Consumer<MenuEvent> closeEvent,
            Map<Integer,ItemStack> items,
            Map<Integer,Consumer<ClickEvent>> clickEvents,
            EnumSet<MenuPermissions> permissions,
            HumanEntity player
    )
    {
        this.items = items;
        this.clickEvents = clickEvents;
        this.defaultClickEvent = defaultClickEvent;
        this.openEvent = openEvent;
        this.closeEvent = closeEvent;
        this.permissions = permissions;
        this.player = player;
    }

}
