package me.vwinter.library.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface MenuAction {

    /**
     * Returns a boolean whether the {@link InventoryClickEvent} should be cancelled or not.
     */
    boolean onClick(final ClickType clickType);

}