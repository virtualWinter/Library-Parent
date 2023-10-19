package me.vwinter.library.sidebar.internal;

import me.vwinter.library.sidebar.Sidebar;
import me.vwinter.library.sidebar.SidebarFactory;
import org.bukkit.entity.Player;

public class InternalSidebarFactory implements SidebarFactory {

    @Override
    public Sidebar createSidebar(Player player) {
        return new InternalSidebar(player);
    }
}
