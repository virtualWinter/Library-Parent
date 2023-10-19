package me.vwinter.library.sidebar;

import org.bukkit.entity.Player;

public interface SidebarService {

    /**
     * Returns a player's sidebar.
     */
    Sidebar getSidebar(final Player player);

    /**
     * Sets a player's sidebar.
     */
    void setSidebar(final Player player, final Sidebar sidebar);

    /**
     * Invalidates a player's sidebar.
     */
    void invalidateSidebar(final Player player);

    /**
     * Returns the {@link SidebarProvider}.
     */
    SidebarProvider getProvider();

    /**
     * Set the {@link SidebarProvider}.
     */
    void setProvider(final SidebarProvider provider);

}
