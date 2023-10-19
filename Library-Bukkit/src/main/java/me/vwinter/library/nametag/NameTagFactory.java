package me.vwinter.library.nametag;

import org.bukkit.entity.Player;

public interface NameTagFactory {

    /**
     * Create a nameTag for a player.
     *
     * @param player the player
     * @return Returns a new nameTag for the player.
     */
    NameTag createNameTag(final Player player);

}
