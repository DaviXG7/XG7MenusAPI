package com.xg7plugins.xg7menus.api.items;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.xg7plugins.xg7menus.api.utils.Log;
import com.xg7plugins.xg7menus.api.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class SkullItem extends Item {
    public SkullItem(int slot, int amount) {
        super(slot, XMaterial.PLAYER_HEAD.parseItem(amount),  null);
    }
    public SkullItem(int slot, int amount, Button button) {
        super(slot, XMaterial.PLAYER_HEAD.parseItem(amount),  button);
    }
    public static SkullItem newSkullItem(int slot, int amount) {
        return new SkullItem(slot, amount);
    }
    public static SkullItem newSkullItem(int slot, int amount, Button button) {
        return new SkullItem(slot, amount,button);
    }

    /**
     * This method sets the skull skin value
     * @param value The skin value of the skull
     * @return This InventoryItem
     */
    public SkullItem setValue(String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "null");
        gameProfile.getProperties().put("textures", new Property("textures", value));

        SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();

        try {
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        super.meta(skullMeta);
        return this;
    }
    /**
     * This method sets the skull owner
     * @param owner The skin owner of the skull
     * @return This InventoryItem
     */
    public SkullItem setOwner(String owner) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) <= 7) return this;
        ((SkullMeta) this.itemStack.getItemMeta()).setOwner(owner);
        return this;
    }

    /**
     * This method sets the skull skin value with the player skin value
     * @param player The player that will be used to get the skin value
     * @return This InventoryItem
     */
    public SkullItem setPlayerSkinValue(UUID player) {
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

            SkullMeta skullMeta = (SkullMeta) this.itemStack.getItemMeta();


            try {
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, gameProfile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
            super.meta(skullMeta);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }
}
