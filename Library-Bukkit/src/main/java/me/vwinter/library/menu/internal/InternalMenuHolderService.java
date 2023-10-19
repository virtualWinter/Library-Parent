package me.vwinter.library.menu.internal;

import com.google.inject.Inject;
import me.vwinter.library.menu.MenuHolder;
import me.vwinter.library.menu.MenuHolderService;
import me.vwinter.library.storage.named.NamedStore;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InternalMenuHolderService implements MenuHolderService {

    private final NamedStore<MenuHolder> store;
    private final Map<Player, MenuHolder> map;

    @Inject
    public InternalMenuHolderService(final NamedStore<MenuHolder> store) {
        this.store = store;
        this.map = new ConcurrentHashMap<>();
    }

    @Override
    public void openHolder(final Player player, final MenuHolder holder) {
        if (map.containsKey(player)) {
            forceCloseHolder(player, true);
        }

        if (!store.isPresent(holder)) {
            store.add(holder);
        }

        map.put(player, holder);
        player.openInventory(holder.getInventory(player));
    }

    @Override
    public void forceCloseHolder(final Player player, final boolean invalidateOnly) {
        if (!invalidateOnly) {
            if (player.getOpenInventory().getTopInventory().getHolder() instanceof MenuHolder) {
                player.closeInventory();
            }
        }

        final MenuHolder holder = map.get(player);
        map.remove(player);
        if (holder != null) {
            holder.invalidate(player);
        }
    }

    @Override
    public MenuHolder getOpenHolder(final Player player) {
        return map.get(player);
    }

    @Override
    public Collection<? extends Player> getPlayersWithOpenHolders() {
        return map.keySet();
    }
}
