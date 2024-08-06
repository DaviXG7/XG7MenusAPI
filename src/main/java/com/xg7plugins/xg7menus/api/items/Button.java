package com.xg7plugins.xg7menus.api.items;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.function.Consumer;

public interface Button {
    void onClick(InventoryClickEvent event);
    void onClick(PlayerInteractEvent event);

    static Button click(Consumer<InventoryClickEvent> eventConsumer) {
        return new Button() {

            @Override
            public void onClick(InventoryClickEvent event) {
                eventConsumer.accept(event);
            }

            @Override
            public void onClick(PlayerInteractEvent event) {}
        };
    }
    static Button playerClick(Consumer<PlayerInteractEvent> eventConsumer) {
        return new Button() {

            @Override
            public void onClick(InventoryClickEvent event) {
            }

            @Override
            public void onClick(PlayerInteractEvent event) {
                eventConsumer.accept(event);
            }
        };
    }
    static Button create(Consumer<InventoryClickEvent> inventoryClickEventConsumer, Consumer<PlayerInteractEvent> playerInteractEventConsumer) {
        return new Button() {
            @Override
            public void onClick(InventoryClickEvent event) {
                inventoryClickEventConsumer.accept(event);
            }

            @Override
            public void onClick(PlayerInteractEvent event) {
                playerInteractEventConsumer.accept(event);
            }
        };
    }

}
