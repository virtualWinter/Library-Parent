package me.vwinter.library.framework.plugin;

import com.google.inject.AbstractModule;
import me.vwinter.library.framework.plugin.annotation.PluginFolder;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.File;

public class PluginModule extends AbstractModule {

    private final Plugin plugin;

    public PluginModule(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        /* Bind default BungeeCord API classes. */
        bind(Plugin.class).toInstance(this.plugin);
        bind(ProxyServer.class).toInstance(this.plugin.getProxy());
        bind(File.class).annotatedWith(PluginFolder.class).toInstance(this.plugin.getDataFolder());
        bind(PluginManager.class).toInstance(this.plugin.getProxy().getPluginManager());
        bind(CommandSender.class).toInstance(this.plugin.getProxy().getConsole());
    }
}
