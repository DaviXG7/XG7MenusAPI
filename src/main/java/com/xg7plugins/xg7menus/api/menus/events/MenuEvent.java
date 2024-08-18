package com.xg7plugins.xg7menus.api.menus.events;

import com.xg7plugins.xg7menus.api.menus.BaseMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class MenuEvent {
    BaseMenu menu;
    Player player;
}
