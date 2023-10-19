package me.vwinter.library.menu;

import com.google.common.collect.Table;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class MenuListener implements Listener {

    private final MenuHolderService service;

    @Inject
    public MenuListener(final MenuHolderService service) {
        this.service = service;
    }

    @EventHandler
    public final void onInventoryCloseEvent(InventoryCloseEvent event) {
        final Player player = (Player) event.getPlayer();
        if (service.getOpenHolder(player) != null) {
            service.forceCloseHolder(player, true);
        }
    }

    @EventHandler
    public final void onPlayerQuitEvent(PlayerQuitEvent event) {
        final Player player = event.getPlayer();
        service.forceCloseHolder(player, true);
    }

    @EventHandler
    public final void onInventoryClickEvent(InventoryClickEvent event) {
        final Player player = (Player) event.getWhoClicked();
        final MenuHolder holder = service.getOpenHolder(player);

        if (holder == null) return;

        Menu menu = holder.getMenu();

        /* Get the menus item-provider and look if the item clicked
         * is in the table */
        for (final Table.Cell<Integer, ItemStack, MenuAction> cell : menu.render(player).cellSet()) {

            assert cell.getColumnKey() != null : "Itemstack is null.";
            assert cell.getRowKey() != null : "Slot is null.";
            assert cell.getValue() != null : "MenuAction is null.";

            if (cell.getRowKey().equals(event.getSlot())) {
                /* Set the boolean whether to cancel this event or not,
                 * based on the return of the menu-actions onClick method. */
                event.setCancelled(cell.getValue().onClick(event.getClick()));
            }

        }

    }


}
