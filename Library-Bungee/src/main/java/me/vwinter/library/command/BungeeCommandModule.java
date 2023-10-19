package me.vwinter.library.command;

import com.google.inject.Key;
import me.vwinter.library.command.parser.BungeeStringParser;
import me.vwinter.library.command.parser.ProxiedPlayerParser;
import me.vwinter.library.command.provider.PlayerProvider;
import me.vwinter.library.command.provider.SenderProvider;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.PluginManager;

public class BungeeCommandModule extends AbstractCommandModule {

    private final PluginManager pluginManager;

    public BungeeCommandModule(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @Override
    public AbstractCommandRegistrar getRegistrar() {
        return new BungeeCommandRegistrar(this.pluginManager);
    }

    @Override
    public void registerDefaults(CommandService service) {
        service.registerParser(Key.get(String.class), new BungeeStringParser());
        service.registerParser(Key.get(ProxiedPlayer.class), new ProxiedPlayerParser());

        service.registerProvider(Key.get(CommandSender.class), new SenderProvider());
        service.registerProvider(Key.get(ProxiedPlayer.class), new PlayerProvider());

    }
}
