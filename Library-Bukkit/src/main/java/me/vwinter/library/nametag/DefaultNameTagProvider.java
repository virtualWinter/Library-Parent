package me.vwinter.library.nametag;

import org.bukkit.entity.Player;

import java.util.function.Function;

public class DefaultNameTagProvider implements NameTagProvider {

    @Override
    public Function<Player, String> getPrefix() {
        return player -> "&7[Prefix] &e";
    }

    @Override
    public Function<Player, String> getSuffix() {
        return player -> " &7(Suffix)";
    }
}
