package me.vwinter.library.sidebar;

import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;

public interface SidebarProvider {

    /**
     * Returns the title.
     */
    Function<Player, String> getTitle();

    /**
     * Returns the lines.
     */
    Function<Player, List<String>> getLines();

    /**
     * Returns a boolean whether the sidebar is showing or not.
     */
    Function<Player, Boolean> isShowing();

}
