package com.xg7plugins.xg7menus.api.gui.builders;

import com.google.gson.Gson;
import com.xg7plugins.xg7menus.api.gui.events.ClickEvent;
import com.xg7plugins.xg7menus.api.gui.MenuItem;
import com.xg7plugins.xg7menus.api.utils.NMSUtil;
import com.xg7plugins.xg7menus.api.utils.Text;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemBuilder {
    protected ItemStack itemStack;
    @Getter
    protected Consumer<ClickEvent> event;

    public ItemBuilder(ItemStack stack) {
        this.itemStack = stack;
    }
    public static ItemBuilder from(Material material) {
        return new ItemBuilder(new ItemStack(material));
    }
    public static ItemBuilder from(MaterialData material) {
        return new ItemBuilder(material.toItemStack());
    }
    public static ItemBuilder from(ItemStack itemStack) {
        return new ItemBuilder(itemStack);
    }
    public static ItemBuilder from(MenuItem item) {
        return new ItemBuilder(item.getItemStack());
    }
    public ItemBuilder setAmount(int amount) {
        this.itemStack.setAmount(amount);
        return this;
    }
    public ItemBuilder setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        return this;
    }
    public ItemBuilder data(MaterialData data) {
        this.itemStack.setData(data);
        return this;
    }
    public ItemBuilder meta(ItemMeta meta) {
        this.itemStack.setItemMeta(meta);
        return this;
    }
    public ItemBuilder addEnchant(Enchantment enchant, int level) {
        this.itemStack.addUnsafeEnchantment(enchant, level);
        return this;
    }
    public ItemBuilder addEnchants(Map<Enchantment, Integer> enchants) {
        this.itemStack.addUnsafeEnchantments(enchants);
        return this;
    }
    public ItemBuilder lore(List<String> lore) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setLore(lore.stream().map(text -> Text.format(text).getText()).collect(Collectors.toList()));
        meta(meta);
        return this;
    }
    public ItemBuilder name(String name) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setDisplayName(Text.format(name).getText());
        meta(meta);
        return this;
    }
    public ItemBuilder addFlags(ItemFlag... flags) {
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.addItemFlags(flags);
        meta(meta);
        return this;
    }
    public ItemBuilder setCustomModelData(int data) {
        if (Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) < 9) return this;
        ItemMeta meta = this.itemStack.getItemMeta();
        meta.setCustomModelData(data);
        meta(meta);
        return this;
    }
    @SneakyThrows
    public ItemBuilder unbreakable(boolean unbreakable) {
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
    public ItemBuilder setPlaceHolders(Player player) {
        if (itemStack.getItemMeta() == null) return this;
        if (itemStack.getItemMeta().getDisplayName() != null) name(Text.format(itemStack.getItemMeta().getDisplayName()).setPlaceholders(player).getText());
        if (itemStack.getItemMeta().getLore() != null) lore(itemStack.getItemMeta().getLore().stream().map(l -> Text.format(l).setPlaceholders(player).getText()).collect(Collectors.toList()));
        return this;
    }
    @SneakyThrows
    public ItemBuilder addOrModifyNBTTag(String key, Object value) {

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
    public ItemStack asItemStack() {
        return this.itemStack;
    }
    public MenuItem asMenuItem() {
        return new MenuItem(this.itemStack);
    }
    public MenuItem asMenuItem(Consumer<ClickEvent> event) {
        this.event = event;
        return new MenuItem(this.itemStack);
    }
    public static ItemBuilder chose(boolean chose, ItemBuilder item1, ItemBuilder item2) {
        return chose ? item1 : item2;
    }
    public static ItemBuilder choseByNewerVersion(int version, ItemBuilder item1, ItemBuilder item2) {
        return Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) > version ? item1 : item2;
    }
    public static ItemBuilder choseByOlderVersion(int version, ItemBuilder item1, ItemBuilder item2) {
        return Integer.parseInt(Bukkit.getServer().getVersion().split("\\.")[1].replace(")", "")) < version ? item1 : item2;
    }
}
