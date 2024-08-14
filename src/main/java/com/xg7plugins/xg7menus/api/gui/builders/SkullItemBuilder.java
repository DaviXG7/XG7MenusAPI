package com.xg7plugins.xg7menus.api.gui.builders;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.xg7plugins.xg7menus.api.gui.MenuItem;
import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.utils.Log;
import com.xg7plugins.xg7menus.api.utils.XSeries.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;
import java.util.function.Consumer;

public class SkullItemBuilder extends ItemBuilder {

    private boolean renderPlayerSkullOnOpen = false;

    public SkullItemBuilder() {
        super(XMaterial.PLAYER_HEAD.parseItem());
    }

    public void renderPlayerSkullOnOpen(boolean renderPlayerSkullOnOpen) {
        this.renderPlayerSkullOnOpen = renderPlayerSkullOnOpen;
    }

    @Override
    public MenuItem asMenuItem() {
        MenuItem menuItem = new MenuItem(this.itemStack);
        menuItem.setRenderIfSkull(renderPlayerSkullOnOpen);
        return menuItem;
    }
    public MenuItem asMenuItem(Consumer<ClickEvent> event) {
        this.event = event;
        MenuItem menuItem = new MenuItem(this.itemStack);
        menuItem.setRenderIfSkull(renderPlayerSkullOnOpen);
        return menuItem;
    }


    /**
     * This method sets the skull skin value
     * @param value The skin value of the skull
     * @return This InventoryItem
     */
    public SkullItemBuilder setValue(String value) {
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
    public SkullItemBuilder setOwner(String owner) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) <= 7) return this;
        ((SkullMeta) this.itemStack.getItemMeta()).setOwner(owner);
        return this;
    }

    /**
     * This method sets the skull skin value with the player skin value
     * @param player The player that will be used to get the skin value
     * @return This InventoryItem
     */
    public SkullItemBuilder setPlayerSkinValue(UUID player) {
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
