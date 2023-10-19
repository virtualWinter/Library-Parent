package me.vwinter.library.sidebar;

import com.google.inject.Inject;
import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class DefaultSidebarProvider implements SidebarProvider {

    private final Server server;

    @Inject
    public DefaultSidebarProvider(final Server server) {
        this.server = server;
    }

    @Override
    public Function<Player, String> getTitle() {
        return player -> "&dVertic";
    }

    @Override
    public Function<Player, List<String>> getLines() {
        return player -> Arrays.asList(
                "&7&m----------------------",
                "&bOnline: &f" + server.getOnlinePlayers().size(),
                " ",
                "&bYou: &f" + player.getName(),
                "&7&m----------------------"
        );
    }

    @Override
    public Function<Player, Boolean> isShowing() {
        return player -> true;
    }
}
