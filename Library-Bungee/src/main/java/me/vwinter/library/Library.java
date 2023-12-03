package me.vwinter.library;

import me.vwinter.library.command.BungeeCommandModule;
import me.vwinter.library.framework.BungeeProject;
import net.md_5.bungee.api.plugin.PluginManager;

import java.util.Optional;

public class Library extends BungeeProject {

    @Override
    public Optional<String> getParentPluginName() {
        return Optional.empty();
    }

    @Override
    public void configure() {
        install(new BungeeCommandModule(getInjector().getInstance(PluginManager.class)));
    }
}
