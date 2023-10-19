package me.vwinter.library.sidebar;

import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class SidebarRunnable implements Runnable {

    private final SidebarService service;
    private final Server server;

    @Inject
    public SidebarRunnable(final SidebarService service, final Server server) {
        this.service = service;
        this.server = server;
    }

    @Override
    public void run() {
        for (final Player player : server.getOnlinePlayers()) {
            final Sidebar sidebar = service.getSidebar(player);
            final SidebarProvider provider = service.getProvider();

            if (sidebar == null) return;

            /* Only hide the sidebar if it's not already hidden */
            if (provider == null || !provider.isShowing().apply(player)) {
                if (!sidebar.isShowing()) {
                    sidebar.hide();
                }

                return;
            }

            /* Only show the sidebar if it's not already shown */
            if (!sidebar.isShowing()) sidebar.show();

            /* Update title and lines from the provider */
            sidebar.updateTitle(provider.getTitle().apply(player));
            sidebar.updateLines(provider.getLines().apply(player));
        }
    }
}
