package com.xg7plugins.xg7menus.api.gui.events;

import com.xg7plugins.xg7menus.api.gui.menus.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class MenuEvent {
    private Player player;
    private Menu menu;
}
