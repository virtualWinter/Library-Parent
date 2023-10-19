package me.vwinter.library.nametag.internal;

import com.google.inject.Inject;
import me.vwinter.library.nametag.NameTag;
import me.vwinter.library.nametag.NameTagProvider;
import me.vwinter.library.nametag.NameTagService;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InternalNameTagService implements NameTagService {

    private final Map<UUID, NameTag> map;
    private NameTagProvider provider;

    @Inject
    public InternalNameTagService(final NameTagProvider provider) {
        this.provider = provider;
        this.map = new HashMap<>();
    }

    @Override
    public NameTag getNameTag(final Player player) {
        return map.get(player.getUniqueId());
    }

    @Override
    public void setNameTag(final Player player, final NameTag nameTag) {
        map.put(player.getUniqueId(), nameTag);
    }

    @Override
    public void invalidateNameTag(final Player player) {
        map.remove(player.getUniqueId());
    }

    @Override
    public NameTagProvider getProvider() {
        return provider;
    }

    @Override
    public void setProvider(final NameTagProvider provider) {
        this.provider = provider;
    }
}
