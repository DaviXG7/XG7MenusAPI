package com.xg7plugins.xg7menus.api.menus;

import com.google.gson.*;

import com.xg7plugins.xg7menus.api.manager.StorageMenuManager;

import com.xg7plugins.xg7menus.api.utils.Log;
import lombok.AllArgsConstructor;

import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * StorageMenu is a menu used like a <br>
 * digital chest, where you can place <br>
 * items in this inventory and store <br>
 * them as you wish.
 * @see Menu Menu <br>
 */
@AllArgsConstructor
public class StorageMenu {

    /**
     * Items that will be stored
     */
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

    /**
     * Will open an Inventory to store item
     * @param player Owner of the inventory
     * @param title Title of the inventory
     * @param size Size of the inventory
     */
    public void open(Player player, String title, int size) {
        StorageMenuManager.add(player);
        Inventory inventory = Bukkit.createInventory(player, size, title);

        items.forEach(inventory::setItem);
        player.openInventory(inventory);
    }

    /**
     * Convert a JSON string to a StorageMenu
     * @param json The JSON that will be converted
     * @return A new StorageMenu with the json's information
     */
    @SneakyThrows
    public static StorageMenu fromString(String json) {
        Map<Integer, ItemStack> items = new HashMap<>();

        JsonArray jsonArray = new JsonParser().parse(json).getAsJsonArray();

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();

            String item64 = jsonObject.get("item").getAsString();
            int slot = jsonObject.get("slot").getAsInt();

            String yaml = new String(Base64.getDecoder().decode(item64));
            YamlConfiguration config = new YamlConfiguration();
            config.loadFromString(yaml);

            items.put(slot, config.getItemStack("item"));
        }
        return new StorageMenu(items);
    }

    /**
     * Convert the StorageMenu to a JSON
     * @return The StorageMenu converted to a JSON
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        List<Map<String, Object>> inventoryItems = new ArrayList<>();
        for (Map.Entry<Integer, ItemStack> stackEntry : items.entrySet()) {
            Map<String, Object> inventoryItem = new HashMap<>();

            YamlConfiguration config = new YamlConfiguration();
            config.set("item", stackEntry.getValue());
            String yaml = config.saveToString();

            inventoryItem.put("item", Base64.getEncoder().encodeToString(yaml.getBytes()));
            inventoryItem.put("slot", stackEntry.getKey());

            inventoryItems.add(inventoryItem);
        }
        Log.info("This is the json of the items: " + gson.toJson(inventoryItems));
        return gson.toJson(inventoryItems);
    }




}
