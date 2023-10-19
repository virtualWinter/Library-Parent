package me.vwinter.library.menu.internal;

import me.vwinter.library.menu.Menu;
import me.vwinter.library.menu.MenuContext;
import me.vwinter.library.menu.MenuHolder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InternalMenuHolder implements MenuHolder {

    private final MenuContext context;
    private final Map<Player, Inventory> map;
    private final Menu menu;
    private final UUID uniqueId;
    private final String name;

    public InternalMenuHolder(final MenuContext context, final Menu menu, final UUID uniqueId, final String name) {
        this.context = context;
        this.map = new ConcurrentHashMap<>();
        this.menu = menu;
        this.uniqueId = uniqueId;
        this.name = name;
    }

    @Override
    public MenuContext getContext() {
        return this.context;
    }

    @Override
    public Inventory getInventory(final Player player) {
        Inventory inventory = map.get(player);

        if (inventory == null) {
            inventory = player.getServer().createInventory(this, getContext().getRows() * 9, getContext().getTitle());
            map.put(player, inventory);
        }

        return inventory;
    }

    @Override
    public void invalidate(Player player) {
        map.remove(player);
    }

    @Override
    public Menu getMenu() {
        return this.menu;
    }

    @Override
    public UUID getUniqueId() {
        return this.uniqueId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Inventory getInventory() {
        return null;
    }
}
