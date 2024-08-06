package com.xg7plugins.xg7menus.api.items;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xg7plugins.xg7menus.api.utils.NMSUtil;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Method;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Item implements Cloneable {

    private int slot;
    protected ItemStack itemStack;
    private Button button;

    public static Item newItem(ItemStack stack, int slot, Button button) {
        return new Item(slot,stack, button);
    }
    public static Item newItem(ItemStack stack, int slot) {
        return new Item(slot,stack,null);
    }
    public Item click(Button button) {
        this.button = button;
        return this;
    }
    public Item setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
    public Item setSlot(int slot) {
        this.slot = slot;
        return this;
    }
    public Item data(MaterialData data) {
        this.itemStack.setData(data);
        return this;
    }
    public Item meta(ItemMeta meta) {
        this.itemStack.setItemMeta(meta);
        return this;
    }
    public Item addEnchant(Enchantment enchant, int level) {
        this.itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }
    public Item addEnchants(Map<Enchantment, Integer> enchants) {
        this.itemStack.addUnsafeEnchantments(enchants);
        return this;
    }
    public Item lore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore.stream().map(Text::translateColorCodes).collect(Collectors.toList()));
        meta(meta);
        return this;
    }
    public Item name(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(Text.translateColorCodes(name));
        meta(meta);
        return this;
    }
    public static Item fromItemStack(ItemStack stack) {
        return new Item(-1, stack, null);
    }
    public Item addFlags(ItemFlag... flags) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(flags);
        meta(meta);
        return this;
    }
    public Item setCustomModelData(int data) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(data);
        meta(meta);
        return this;
    }
    @SneakyThrows
    public Item unbreakable(boolean unbreakable) {
        ItemMeta meta = this.itemStack.getItemMeta();
        try {
            meta.setUnbreakable(unbreakable);
        } catch (Exception ignored) {
            Object spigot = meta.getClass().getMethod("spigot").invoke(meta);
            spigot.getClass().getMethod("setUnbreakable", Boolean.class).invoke(spigot, unbreakable);
        }
        meta(meta);
        return this;
    }
    public Item setPlaceHolders(Player player) {
        if (itemStack.getItemMeta() == null) return this;
        if (itemStack.getItemMeta().getDisplayName() != null) name(Text.setPlaceholders(itemStack.getItemMeta().getDisplayName(), player));
        if (itemStack.getItemMeta().getLore() != null) lore(itemStack.getItemMeta().getLore().stream().map(l -> Text.setPlaceholders(l,player)).collect(Collectors.toList()));
        return this;
    }

    @SneakyThrows
    public Item addOrModifyNBTTag(String key, Object value) {

        Gson gson = new Gson();

        Class<?> craftItemStackClass = NMSUtil.getCraftBukkitClass("inventory.CraftItemStack");

        Class<?> nmsItemStackClass = NMSUtil.getNMSClass("ItemStack");

        Class<?> nbtTagCompoundClass = NMSUtil.getNMSClass("NBTTagCompound");

        Object nmsItem = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);

        Object tag = nmsItemStackClass.getMethod("getTag").invoke(nmsItem);

        if (tag == null) tag = nbtTagCompoundClass.getDeclaredConstructor().newInstance();

        String jsonValue = gson.toJson(value);

        Method setStringMethod = nbtTagCompoundClass.getMethod("setString", String.class, String.class);
        setStringMethod.invoke(tag, key, jsonValue);

        Method setTagMethod = nmsItemStackClass.getMethod("setTag", nbtTagCompoundClass);
        setTagMethod.invoke(nmsItem, tag);

        Method asBukkitCopyMethod = craftItemStackClass.getMethod("asBukkitCopy", nmsItemStackClass);

        this.itemStack = (ItemStack) craftItemStackClass.getMethod("asBukkitCopy", nmsItemStackClass).invoke(null, nmsItem);
        return this;
    }

    @SneakyThrows
    public <T> T getNBTTagValue(String key, Class<T> clazz) {
        Gson gson = new Gson();

        Class<?> craftItemStackClass = NMSUtil.getCraftBukkitClass("inventory.CraftItemStack");

        Class<?> nmsItemStackClass = NMSUtil.getNMSClass("ItemStack");

        Class<?> nbtTagCompoundClass = NMSUtil.getNMSClass("NBTTagCompound");

        Object nmsItem = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class).invoke(null, itemStack);

        Object tag = nmsItemStackClass.getMethod("getTag").invoke(nmsItem);

        if (tag != null) {
            String jsonValue = (String) nbtTagCompoundClass.getMethod("getString", String.class).invoke(tag, key);
            return gson.fromJson(jsonValue, clazz);
        }
        return null;
    }

    @SneakyThrows
    public static Item fromString(String json) {
        JsonObject object = new JsonParser().parse(json).getAsJsonObject();

        String item64 = object.get("item").getAsString();

        String yaml = new String(Base64.getDecoder().decode(item64));
        YamlConfiguration config = new YamlConfiguration();
        config.loadFromString(yaml);

        return new Item(object.get("slot").getAsInt(), config.getItemStack("item"), null);
    }


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
    public static Item chose(boolean chose, Item item1, Item item2) {
        return chose ? item1 : item2;
    }
    public static Item choseByNewerVersion(int version, Item item1, Item item2) {
        return Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) > version ? item1 : item2;
    }
    public static Item choseByOlderVersion(int version, Item item1, Item item2) {
        return Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) < version ? item1 : item2;
    }

    @SneakyThrows
    @Override
    public Item clone() {
        return (Item) super.clone();
    }
}
