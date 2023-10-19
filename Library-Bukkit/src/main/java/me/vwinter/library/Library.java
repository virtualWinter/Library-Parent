package me.vwinter.library;

import me.vwinter.library.command.BukkitCommandModule;
import me.vwinter.library.example.ExampleModule;
import me.vwinter.library.framework.BukkitProject;
import me.vwinter.library.menu.MenuModule;
import me.vwinter.library.nametag.NameTagModule;
import me.vwinter.library.sidebar.SidebarModule;
import org.bukkit.command.CommandMap;

import java.util.Optional;

public class Library extends BukkitProject {

    @Override
    public Optional<String> getParentPluginName() {
        return Optional.empty();
    }

    @Override
    public void configure() {
        install(new BukkitCommandModule(getInjector().getInstance(CommandMap.class)));
        install(new MenuModule());
        install(new SidebarModule());
        install(new NameTagModule());
        install(new ExampleModule());
    }
}
