package me.vwinter.library.menu;

import com.google.common.collect.Table;
import com.google.inject.Inject;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuRunnable implements Runnable {

    private final MenuHolderService service;

    @Inject
    public MenuRunnable(final MenuHolderService service) {
        this.service = service;
    }

    @Override
    public void run() {
        /* Iterate through players and update their inventory from
         * their holder's menu renderer. */
        for (final Player player : service.getPlayersWithOpenHolders()) {
            if (player.getOpenInventory() == null) {
                continue;
            }

            final MenuHolder holder = service.getOpenHolder(player);
            final Inventory inventory = holder.getInventory(player);
            final Menu menu = holder.getMenu();

            for (final Table.Cell<Integer, ItemStack, MenuAction> cell : menu.render(player).cellSet()) {
                /* Assert that all the contents of the cells are nonnull. */
                assert cell.getColumnKey() != null : "Itemstack is null.";
                assert cell.getRowKey() != null : "Slot is null.";
                assert cell.getValue() != null : "MenuAction is null.";

                inventory.setItem(cell.getRowKey(), cell.getColumnKey());
            }

            player.updateInventory();
        }
    }
}
