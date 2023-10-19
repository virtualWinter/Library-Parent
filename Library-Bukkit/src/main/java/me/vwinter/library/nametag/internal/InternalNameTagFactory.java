package me.vwinter.library.nametag.internal;

import me.vwinter.library.nametag.NameTag;
import me.vwinter.library.nametag.NameTagFactory;
import org.bukkit.entity.Player;

public class InternalNameTagFactory implements NameTagFactory {

    @Override
    public NameTag createNameTag(final Player player) {
        return new InternalNameTag(player);
    }
}
