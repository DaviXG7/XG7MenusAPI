package com.xg7plugins.xg7menus.api;

import com.xg7plugins.xg7menus.api.menus.builders.menu.MenuBuilder;
import com.xg7plugins.xg7menus.api.menus.listeners.MenuListener;
import com.xg7plugins.xg7menus.api.menus.listeners.PlayerMenuListener;
import com.xg7plugins.xg7menus.api.menus.player.PlayerMenu;
import com.xg7plugins.xg7menus.api.utils.Log;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;

public class XG7Menus {

    @Getter
    private static JavaPlugin plugin;

    public static void setDebug(boolean debug) {
        Log.setEnabled(debug);
    }

    public static void inicialize(JavaPlugin javaPlugin) {
        plugin = javaPlugin;
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(), plugin);
        plugin.getServer().getPluginManager().registerEvents(new PlayerMenuListener(), plugin);
        Bukkit.getConsoleSender().sendMessage("[" + plugin.getName() + "] is using XG7MenusAPI.");
        Bukkit.getConsoleSender().sendMessage("It's a free api, visit our website https://xg7plugins.com");
    }
    public static void disable() {
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
        for (PlayerMenu menu : PlayerMenu.getPlayerMenuMap().values()) menu.clear();
    }

}