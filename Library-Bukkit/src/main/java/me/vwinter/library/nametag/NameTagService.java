package me.vwinter.library.nametag;

import org.bukkit.entity.Player;

public interface NameTagService {

    /**
     * Returns a player's nameTag.
     */
    NameTag getNameTag(final Player player);

    /**
     * Sets a player's nameTag.
     */
    void setNameTag(final Player player, final NameTag nameTag);

    /**
     * Invalidates a player's nameTag.
     */
    void invalidateNameTag(final Player player);

    /**
     * Returns the {@link NameTagProvider}.
     */
    NameTagProvider getProvider();

    /**
     * Set the {@link NameTagProvider}.
     */
    void setProvider(final NameTagProvider provider);

}
