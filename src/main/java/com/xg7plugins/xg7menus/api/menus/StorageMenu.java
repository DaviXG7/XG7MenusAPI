package com.xg7plugins.xg7menus.api.menus;

import com.xg7plugins.xg7menus.api.manager.StorageMenuManager;
import lombok.AllArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

@AllArgsConstructor
public class StorageMenu {

    private Map<Integer, ItemStack> items;

    public StorageMenu(Inventory inventory) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) continue;
            items.put(i, inventory.getItem(i));
        }
    }

    public void open(Player player, String title, int size) {
        StorageMenuManager.add(player);
        Inventory inventory = Bukkit.createInventory(player, size, title);

        items.entrySet().forEach(item -> player.getInventory().setItem(item.getKey(), item.getValue()));
        player.openInventory(inventory);
    }
    public StorageMenu save(Inventory inventory) {
        IntStream.range(0, inventory.getSize()).filter(i -> inventory.getItem(i) != null && inventory.getItem(i).getType() != Material.AIR).forEach(i -> items.put(i, inventory.getItem(i)));
        return this;
    }

    public static StorageMenu fromString(String json) {
        JSONArray array = new JSONArray(json);
        HashMap<Integer, ItemStack> items = new HashMap<>();
        array.forEach(item -> {
            JSONObject object = new JSONObject(item);
            items.put(object.getInt("slot"), ItemStack.deserialize((Map<String, Object>) object.get("item")));
        });
        return new StorageMenu(items);
    }

    @Override
    public String toString() {
        JSONArray array = new JSONArray();
        for (Map.Entry<Integer, ItemStack> item : items.entrySet()) {
            JSONObject object = new JSONObject();
            object.put("slot", item.getKey());
            object.put("item", item.getValue().serialize());
            array.put(object);
        }

        return array.toString();
    }




}
