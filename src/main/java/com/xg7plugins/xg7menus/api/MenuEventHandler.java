package com.xg7plugins.xg7menus.api;

import com.xg7plugins.xg7menus.api.items.Item;
import com.xg7plugins.xg7menus.api.menus.Menu;
import com.xg7plugins.xg7menus.api.menus.PlayerMenu;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenuEventHandler implements Listener {

    @Getter
    private static final Map<UUID, PlayerMenu> playerMenus = new HashMap<>();
    @Getter
    private static final Map<UUID, Map<Integer, ItemStack>> olditems = new HashMap<>();

    public static void register(Player player, PlayerMenu menu) {
        olditems.put(player.getUniqueId(), IntStream.range(0, player.getInventory().getSize()).filter(i -> player.getInventory().getItem(i) != null).boxed().collect(Collectors.toMap(i -> i, i -> player.getInventory().getItem(i), (a, b) -> b)));
        playerMenus.put(player.getUniqueId(), menu);
    }
    public static void unregister(Player player) {
        olditems.remove(player.getUniqueId());
        playerMenus.remove(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getHolder() instanceof Menu) {
            Menu menu = (Menu) event.getClickedInventory().getHolder();
            for (Item item1 : menu.getItems())
                if (item1.getSlot() == event.getSlot()) {
                    if (item1.getButton() != null) {
                        event.setCancelled(menu.isCancelInteraction());
                        item1.getButton().onClick(event);
                        return;
                    }
                    break;
                }
            event.setCancelled(menu.isCancelInteraction());
            if (menu.getClickListener() != null) menu.getClickListener().onClick(event);
            return;
        }
        if (event.getClickedInventory().getHolder() instanceof Player) {
            Player player = (Player) event.getWhoClicked();
            if (!playerMenus.containsKey(player.getUniqueId())) return;

            PlayerMenu playerMenu = playerMenus.get(player.getUniqueId());
            for (Item item1 : playerMenu.getItems())
                if (item1.getSlot() == event.getSlot()) {
                    if (item1.getButton() != null) {
                        event.setCancelled(playerMenu.isCancelInteraction());
                        item1.getButton().onClick(event);
                        return;
                    }
                    break;
                }
            event.setCancelled(playerMenu.isCancelInteraction());
            if (playerMenu.getClickListener() != null) playerMenu.getClickListener().onClick(event);
        }

    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {

        Player player = event.getPlayer();
        if (!playerMenus.containsKey(player.getUniqueId())) return;

        PlayerMenu playerMenu = playerMenus.get(player.getUniqueId());
        for (Item item1 : playerMenu.getItems())
            if (item1.getSlot() == player.getInventory().getHeldItemSlot()) {
                if (item1.getButton() != null) {
                    event.setCancelled(playerMenu.isCancelInteraction());
                    item1.getButton().onClick(event);
                    return;
                }
                break;
            }
        event.setCancelled(playerMenu.isCancelInteraction());
        if (playerMenu.getClickListener() != null) playerMenu.getClickListener().onClick(event);

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent event) {
        if (event.getInventory().getHolder() instanceof Menu) {
            Menu menu = (Menu) event.getInventory().getHolder();
            if (menu.getMenuListener() == null) return;
            menu.getMenuListener().onOpen((Player) event.getPlayer());
        }
    }
    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (event.getInventory().getHolder() instanceof Menu) {
            Menu menu = (Menu) event.getInventory().getHolder();
            if (menu.getMenuListener() == null) return;
            menu.getMenuListener().onClose((Player) event.getPlayer());
        }
    }

}
