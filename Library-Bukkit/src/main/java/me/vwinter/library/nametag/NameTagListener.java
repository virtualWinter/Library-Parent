package me.vwinter.library.nametag;

import com.google.inject.Inject;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class NameTagListener implements Listener {

    private final NameTagService service;
    private final NameTagFactory factory;

    @Inject
    public NameTagListener(final NameTagService service, final NameTagFactory factory) {
        this.service = service;
        this.factory = factory;
    }

    @EventHandler
    public final void onPlayerJoinEvent(PlayerJoinEvent event) {
        service.setNameTag(event.getPlayer(), factory.createNameTag(event.getPlayer()));
    }

    @EventHandler
    public final void onPlayerQuitEvent(PlayerQuitEvent event) {
        service.invalidateNameTag(event.getPlayer());
    }
}
