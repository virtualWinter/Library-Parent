package me.vwinter.library.sidebar.internal;

import com.google.inject.Inject;
import me.vwinter.library.sidebar.Sidebar;
import me.vwinter.library.sidebar.SidebarProvider;
import me.vwinter.library.sidebar.SidebarService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InternalSidebarService implements SidebarService {

    private final Map<UUID, Sidebar> map;
    private SidebarProvider provider;

    @Inject
    public InternalSidebarService(final SidebarProvider provider) {
        this.provider = provider;
        this.map = new HashMap<>();
    }

    @Override
    public Sidebar getSidebar(final Player player) {
        return map.get(player.getUniqueId());
    }

    @Override
    public void setSidebar(final Player player, final Sidebar sidebar) {
        map.put(player.getUniqueId(), sidebar);
    }

    @Override
    public void invalidateSidebar(final Player player) {
        map.remove(player.getUniqueId());
    }

    @Override
    public SidebarProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(final SidebarProvider provider) {
        this.provider = provider;
    }
}
