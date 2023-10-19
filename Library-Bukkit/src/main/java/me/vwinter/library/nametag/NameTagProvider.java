package me.vwinter.library.nametag;

import org.bukkit.entity.Player;

import java.util.function.Function;

public interface NameTagProvider {

    /**
     * Returns the prefix.
     */
    Function<Player, String> getPrefix();

    /**
     * Returns the suffix.
     */
    Function<Player, String> getSuffix();

}
