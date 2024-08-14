package com.xg7plugins.xg7menus.api.gui.events;

import com.xg7plugins.xg7menus.api.gui.menus.Menu;
import com.xg7plugins.xg7menus.api.gui.MenuItem;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Cancellable;

@RequiredArgsConstructor
@Getter
public class ClickEvent implements Cancellable {

    private boolean cancelled = true;
    private final HumanEntity player;
    private final ClickAction clickAction;
    private final int clickedSlot;
    private final MenuItem clickedItem;
    private final Menu clickedMenu;
    private final Location locationClicked;

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

    public enum ClickAction {
        LEFT,
        SHIFT_LEFT,
        RIGHT,
        SHIFT_RIGHT,
        WINDOW_BORDER_LEFT,
        WINDOW_BORDER_RIGHT,
        MIDDLE,
        NUMBER_KEY,
        DOUBLE_CLICK,
        DROP,
        CONTROL_DROP,
        CREATIVE,
        SWAP_OFFHAND,
        UNKNOWN,
        LEFT_CLICK_BLOCK,
        RIGHT_CLICK_BLOCK,
        LEFT_CLICK_AIR,
        RIGHT_CLICK_AIR,
        PHYSICAL
    }

}
