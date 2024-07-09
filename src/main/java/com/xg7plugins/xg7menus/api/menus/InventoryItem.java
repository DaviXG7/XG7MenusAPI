package com.xg7plugins.xg7menus.api.menus;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.xg7plugins.xg7menus.api.utils.Log;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class InventoryItem {

    private final ItemStack itemStack;
    @Setter(AccessLevel.PROTECTED)
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

    public InventoryItem addEnchant(Enchantment enchant, int level) {
        this.itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }
    public InventoryItem addEnchants(Map<Enchantment, Integer> enchants) {
        this.itemStack.addUnsafeEnchantments(enchants);
        return this;
    }
    public void setMeta(ItemMeta meta) {
        this.itemStack.setItemMeta(meta);
    }
    public void setPlaceholders(Player player) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(Text.setPlaceholders(meta.getDisplayName(), player));
        meta.setLore(meta.getLore().stream().map(l -> Text.setPlaceholders(l, player)).collect(Collectors.toList()));
    }

    static class SkullInventoryItem extends InventoryItem {

        public SkullInventoryItem(String name, List<String> lore, int amount, int slot) {
            super(Arrays.asList(Material.values()).stream().map(Material::name).collect(Collectors.toList()).contains("PLAYER_HEAD")
                    ? new MaterialData(Material.getMaterial("PLAYER_HEAD")) : new MaterialData(Material.getMaterial("SKULL_ITEM"), (byte) 3),
                    name, lore, amount, slot);
        }

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
        public SkullInventoryItem setOwner(String owner, boolean onlineMode) {
            ((SkullMeta) super.getItemStack().getItemMeta()).setOwner(owner);
            if (onlineMode) setPlayerSkinValue((Player) Bukkit.getOfflinePlayer(owner));
            return this;
        }
        public SkullInventoryItem setPlayerSkinValue(Player player) {
            try {
                URL url = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + player.getUniqueId());

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

                JSONObject profileData = new JSONObject(sb.toString());
                JSONObject properties = profileData.getJSONArray("properties").getJSONObject(0);


                GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
                gameProfile.getProperties().put("textures", new Property("textures", properties.getString("value")));

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

    public static InventoryItem fromString(String s) {
        JSONObject object = new JSONObject(s);
        return new InventoryItem(ItemStack.deserialize((Map<String, Object>) object.get("item")), object.getInt("slot"));
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("slot", slot);
        jsonObject.put("item", itemStack.serialize());
        return jsonObject.toString();
    }


}
