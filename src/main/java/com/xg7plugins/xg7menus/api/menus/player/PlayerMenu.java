package com.xg7plugins.xg7menus.api.menus.player;

import com.xg7plugins.xg7menus.api.menus.BaseMenu;
import com.xg7plugins.xg7menus.api.menus.MenuPermissions;
import com.xg7plugins.xg7menus.api.menus.builders.item.ItemBuilder;
import com.xg7plugins.xg7menus.api.menus.events.ClickEvent;
import com.xg7plugins.xg7menus.api.menus.events.MenuEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class PlayerMenu extends BaseMenu {

    @Getter
    private static final Map<UUID,PlayerMenu> playerMenuMap = new HashMap<>();

    private final Map<Integer, ItemStack> oldItems = new HashMap<>();

    public PlayerMenu(Consumer<ClickEvent> defaultClickEvent, Consumer<MenuEvent> openEvent, Consumer<MenuEvent> closeEvent, Map<Integer, ItemStack> items, Map<Integer, Consumer<ClickEvent>> clickEvents, EnumSet<MenuPermissions> permissions, Player player) {
        super(defaultClickEvent, openEvent, closeEvent, items, clickEvents,permissions,player);
    }
    public void give() {
        IntStream.range(0, player.getInventory().getSize()).filter(i -> player.getInventory().getItem(i) != null).forEach(i -> oldItems.put(i, player.getInventory().getItem(i)));
        player.getInventory().clear();
        items.forEach((key, value) -> player.getInventory().setItem(key, value));
        playerMenuMap.put(player.getUniqueId(),this);
    }
    public void clear() {
        player.getInventory().clear();
        oldItems.forEach((key, value) -> player.getInventory().setItem(key, value));
        oldItems.clear();
        playerMenuMap.remove(player.getUniqueId());
    }

    public void setItem(int slot, ItemStack item) {
        items.put(slot, item);
    }
    public void setItem(int slot, ItemStack item, Consumer<ClickEvent> clickEvent) {
        items.put(slot, item);
        clickEvents.put(slot, clickEvent);
    }
    public void setClickEvent(int slot, Consumer<ClickEvent> clickEvent) {
        clickEvents.put(slot, clickEvent);
    }

    public void update() {
        items.forEach((key, value) -> player.getInventory().setItem(key, value));
    }

    public void updateItem(int slot, ItemBuilder builder) {
        player.getInventory().setItem(slot, builder.toItemStack());
        items.put(slot, builder.toItemStack());
        if (builder.getEvent() != null) clickEvents.put(slot, builder.getEvent());
        else clickEvents.remove(slot);
    }
}
