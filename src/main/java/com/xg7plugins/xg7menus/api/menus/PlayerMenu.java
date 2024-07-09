package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.manager.MenuManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class PlayerMenu extends Menu {

    private final Map<Integer, ItemStack> oldItems;

    public PlayerMenu(String id) {
        super(id, "", 0);
        oldItems = new HashMap<>();
    }

    @Override
    public void open(Player player) {

        IntStream.range(0, player.getInventory().getSize()).filter(i -> player.getInventory().getItem(i) != null && player.getInventory().getItem(i).getType() != Material.AIR).forEach(i -> oldItems.put(i, player.getInventory().getItem(i)));

        MenuManager.put(player, this);

        player.getInventory().clear();

        for (InventoryItem item : getItems()) {
            item.setPlaceholders(player);
            player.getInventory().setItem(item.getSlot(), item.getItemStack());
        }

    }
    public void close(Player player) {
        player.getInventory().clear();
        oldItems.entrySet().forEach(oldItem -> player.getInventory().setItem(oldItem.getKey(), oldItem.getValue()));
    }

}
