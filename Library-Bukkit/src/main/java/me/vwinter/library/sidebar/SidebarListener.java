package me.vwinter.library.sidebar;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class SidebarListener implements Listener {

    private final SidebarService service;
    private final SidebarFactory factory;

    @Inject
    public SidebarListener(final SidebarService service, final SidebarFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @EventHandler
    public final void onPlayerJoinEvent(PlayerJoinEvent event) {
        service.setSidebar(event.getPlayer(), factory.createSidebar(event.getPlayer()));
    }

    @EventHandler
    public final void onPlayerQuitEvent(PlayerQuitEvent event) {
        service.invalidateSidebar(event.getPlayer());
    }
}
