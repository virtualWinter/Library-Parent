package me.vwinter.library.menu;

import me.vwinter.library.storage.Storable;
import me.vwinter.library.storage.named.Nameable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public interface MenuHolder extends InventoryHolder, Storable, Nameable {

    /**
     * Returns the {@link MenuContext} for this holder.
     */
    MenuContext getContext();

    /**
     * Returns an inventory representing the menu for the player.
     */
    Inventory getInventory(final Player player);

    /**
     * Invalidated the player from this holder.
     */
    void invalidate(final Player player);

    /**
     * Returns this holder's menu.
     */
    Menu getMenu();

}
