package com.xg7plugins.xg7menus.api.menus;

import com.google.gson.*;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.xg7plugins.xg7menus.api.utils.Log;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * InventoryItem is a simple ItemStack builder <br>
 * where you can modify the itemStack's meta in<br>
 * a much easier way and place it in the menu effortlessly.
 */
@Getter
public class InventoryItem {

    private final ItemStack itemStack;
    @Setter
    private int slot;
    public InventoryItem(Material material, String name, List<String> lore, int amount, int slot) {

        ItemStack itemStack = new ItemStack(material, amount);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        this.itemStack = itemStack;
        this.slot = slot;
    }
    public InventoryItem(ItemStack stack, String name, List<String> lore, int amount, int slot) {

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        stack.setItemMeta(meta);

        stack.setAmount(amount);

        this.itemStack = stack;
        this.slot = slot;
    }

    /**
     * @param material If you are using older versions, you can use materialData to make an item
     */
    public InventoryItem(MaterialData material, String name, List<String> lore, int amount, int slot) {

        ItemStack itemStack = material.toItemStack(amount);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);

        itemStack.setItemMeta(meta);

        this.itemStack = itemStack;
        this.slot = slot;
    }
    public InventoryItem(ItemStack stack, int slot) {
        this.itemStack = stack;
        this.slot = slot;
    }

    /**
     * This method adds an Enchantment to the itemStack
     * @param enchant The enchantment that will be added to the item
     * @param level the level of the enchantment
     * @return This InventoryItem
     */
    public InventoryItem addEnchant(Enchantment enchant, int level) {
        this.itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }
    /**
     * This method adds a List of enchantments to the itemStack
     * @param enchants The map of the enchantment
     * @return This InventoryItem
     */
    public InventoryItem addEnchants(Map<Enchantment, Integer> enchants) {
        this.itemStack.addUnsafeEnchantments(enchants);
        return this;
    }

    /**
     * This method sets a ItemMeta to the itemStack <br>
     * (You can make a custom meta)
     * @param meta The itemMeta that will be set on item
     */
    public void setMeta(ItemMeta meta) {
        this.itemStack.setItemMeta(meta);
    }

    /**
     * If you have the plugin PlaceHolderAPI, this method <br>
     * will replace all placeholders
     * @param player The player of teh placeholder
     */
    public void setPlaceholders(Player player) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(Text.setPlaceholders(meta.getDisplayName(), player));
        if (meta.getLore() != null) meta.setLore(meta.getLore().stream().map(l -> Text.setPlaceholders(l, player)).collect(Collectors.toList()));
        setMeta(meta);
    }

    /**
     * Adds item flags to the item
     * @param flags the flags of the item
     * @return this
     */
    public InventoryItem addFlags(ItemFlag... flags) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(flags);
        setMeta(meta);
        return this;
    }

    /**
     * Adds a custom model data to item <br>
     * (Works +1.9)
     * @param data the data of the model
     * @return this
     */
    public InventoryItem setCustomModelData(int data) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(data);
        setMeta(meta);
        return this;
    }

    /**
     * Sets the item unbreakable
     * @param unbreakable if the item is being unbreakable
     * @return this
     */
    @SneakyThrows
    public InventoryItem setUnbreakable(boolean unbreakable) {
        ItemMeta meta = this.itemStack.getItemMeta();
        try {
            meta.setUnbreakable(unbreakable);
        } catch (Exception ignored) {
            Object spigot = meta.getClass().getMethod("spigot").invoke(meta);
            spigot.getClass().getMethod("setUnbreakable", Boolean.class).invoke(spigot, unbreakable);
        }
        setMeta(meta);
        return this;
    }

    /**
     * The SkullInventoryItem is an InventoryItem <br>
     * where you can make a custom skull.
     */
    public static class SkullInventoryItem extends InventoryItem {

        public SkullInventoryItem(String name, List<String> lore, int amount, int slot) {
            super(Arrays.asList(Material.values()).stream().map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD")
                    ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : new MaterialData(Material.getMaterial("SKULL_ITEM"), (byte) 3),
                    name, lore, amount, slot);
        }

        /**
         * This method sets the skull skin value
         * @param value The skin value of the skull
         * @return This InventoryItem
         */
        public SkullInventoryItem setValue(String value) {
            GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "null");
            gameProfile.getProperties().put("textures", new Property("textures", value));

            SkullMeta skullMeta = (SkullMeta) this.getItemStack().getItemMeta();

            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }

            super.setMeta(skullMeta);
            return this;
        }
        /**
         * This method sets the skull owner
         * @param owner The skin owner of the skull
         * @param onlineMode If the server is in online mode, it will be possible to set the player's skin.
         * @return This InventoryItem
         */
        public SkullInventoryItem setOwner(String owner, boolean onlineMode) {
            ((SkullMeta) super.getItemStack().getItemMeta()).setOwner(owner);
            if (onlineMode) setPlayerSkinValue(Bukkit.getOfflinePlayer(owner).getUniqueId());
            return this;
        }

        /**
         * This method sets the skull skin value with the player skin value
         * @param player The player that will be used to get the skin value
         * @return This InventoryItem
         */
        public SkullInventoryItem setPlayerSkinValue(UUID player) {
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + player);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");


                if (conn.getResponseCode() != 200) {
                    Log.severe("Erro ao colocar valor de player na skin da cabeça!");
                    return this;
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder sb = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    sb.append(output);
                }
                conn.disconnect();

                JsonObject profileData = new JsonParser().parse(sb.toString()).getAsJsonObject();
                JsonObject properties = profileData.getAsJsonArray("properties").get(0).getAsJsonObject();


                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                gameProfile.getProperties().put("textures", new Property("textures", properties.get("value").getAsString()));

                SkullMeta skullMeta = (SkullMeta) super.getItemStack().getItemMeta();


                try {
                    Field profileField = skullMeta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(skullMeta, gameProfile);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                super.setMeta(skullMeta);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return this;
        }
    }

    /**
     * Convert a JSON string to a InventoryItem
     * @param json The JSON that will be converted
     * @return A new InventoryItem with the json's information
     */
    @SneakyThrows
    public static InventoryItem fromString(String json) {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();

        String item64 = object.get("item").getAsString();

        String yaml = new String(Base64.getDecoder().decode(item64));
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(yaml);

        return new InventoryItem(config.getItemStack("item"), object.get("slot").getAsInt());
    }

    /**
     * Convert the InventoryItem to a JSON
     * @return The InventoryItem converted to a JSON
     */
    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", itemStack);
        String yaml = config.saveToString();

        Map<String, Object> inventoryItem = new HashMap<>();
        inventoryItem.put("item", Base64.getEncoder().encodeToString(yaml.getBytes()));
        inventoryItem.put("slot", slot);

        return gson.toJson(inventoryItem);
    }


}
