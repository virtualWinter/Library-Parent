package me.vwinter.library.sidebar;

import org.bukkit.entity.Player;

public interface SidebarFactory {

    /**
     * Create a sidebar for the player.
     *
     * @param player the player.
     * @return Returns a new sidebar for the player.
     */
    Sidebar createSidebar(final Player player);

}
