package com.xg7plugins.xg7menus.api.menus;

import com.google.gson.*;

import com.xg7plugins.xg7menus.api.manager.StorageMenuManager;

import com.xg7plugins.xg7menus.api.utils.NMSUtil;
import lombok.AllArgsConstructor;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.util.*;

@AllArgsConstructor
public class StorageMenu {

    private Map<Integer, ItemStack> items;

    public StorageMenu(Inventory inventory) {
        this.items = new HashMap<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) == null || inventory.getItem(i).getType().equals(Material.AIR)) continue;
            items.put(i, inventory.getItem(i));
        }
    }
    public StorageMenu () {
        this.items = new HashMap<>();
    }

    public void open(Player player, String title, int size) {
        StorageMenuManager.add(player);
        Inventory inventory = Bukkit.createInventory(player, size, title);

        items.forEach(inventory::setItem);
        player.openInventory(inventory);
    }

    @SneakyThrows
    public static StorageMenu fromString(String json) {
        Gson gson = new Gson();

        Map<Integer, ItemStack> items = new HashMap<>();

        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            JsonObject itemObject = jsonObject.getAsJsonObject("item");
            int slot = jsonObject.get("slot").getAsInt();

            Map<String, Object> itemMap = gson.fromJson(itemObject, Map.class);

            items.put(slot, ItemStack.deserialize(itemMap));
        }
        return new StorageMenu(items);
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Map<String, Object>> inventoryItems = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> stackEntry : items.entrySet()) {

            Map<String, Object> item = stackEntry.getValue().serialize();
            item.put("meta", stackEntry.getValue().getItemMeta().serialize());

            Map<String, Object> inventoryItem = new HashMap<>();
            inventoryItem.put("item", item);
            inventoryItem.put("slot", stackEntry.getKey());

            inventoryItems.add(inventoryItem);
        }
        return gson.toJson(inventoryItems);
    }




}
