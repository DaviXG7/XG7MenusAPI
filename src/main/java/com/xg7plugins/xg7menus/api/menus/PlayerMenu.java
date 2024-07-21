package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * PlayerMenu is a menu where click actions <br>
 * will be performed in the player's inventory, <br>
 * both in the hotbar and within their inventory. <br>
 * It is specially designed for lobby menus, <br>
 * menus for placing a region, editing something, etc.
 *
 * @see Menu
 */
@Getter
public class PlayerMenu extends Menu {

    /**
     * These are the items stored before <br>
     * the inventory was opened, and you can retrieve them.
     */
    private final Map<Integer, ItemStack> oldItems = new HashMap<>();

    /**
     * These values cancel the events related to their names.
     */
    @Setter
    private boolean canInteract = false;
    @Setter
    private boolean canBreak = false;
    @Setter
    private boolean canBuild = false;

    public PlayerMenu(String id) {
        super(id, "", 0);
    }

    /**
     * Updates the player inventory
     * @param player The player with inventory
     * @param item The item that will be replaced
     */
    @Override
    public void updateInventory(Player player, InventoryItem item) {
        player.getInventory().setItem(item.getSlot(), item.getItemStack());
    }

    /**
     * Gives the menu items to the player <br>
     * and start the interaction
     * @param player Player who will receive the items.
     */
    @Override
    public void open(Player player) {

        IntStream.range(0, player.getInventory().getSize()).filter(i -> player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR).forEach(i -> oldItems.put(i, player.getInventory().getItem(i)));

        MenuManager.putPlayerMenu(player, this);

        player.getInventory().clear();

        for (InventoryItem item : getItems()) {
            item.setPlaceholders(player);
            player.getInventory().setItem(item.getSlot(), item.getItemStack());
        }

    }

    /**
     * Closes the menu and give back the items
     * @param player Player who will close the inventory
     */
    @Override
    public void close(Player player) {
        player.getInventory().clear();
        MenuManager.removePlayerMenu(player);
        oldItems.entrySet().forEach(oldItem -> player.getInventory().setItem(oldItem.getKey(), oldItem.getValue()));
    }

}
