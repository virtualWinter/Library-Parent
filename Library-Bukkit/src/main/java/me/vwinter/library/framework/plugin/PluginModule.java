package me.vwinter.library.framework.plugin;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import me.vwinter.library.framework.plugin.annotation.DefaultWorld;
import me.vwinter.library.framework.plugin.annotation.MainScoreboard;
import me.vwinter.library.framework.plugin.annotation.PluginFolder;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.io.File;
import java.lang.reflect.Field;

public class PluginModule extends AbstractModule {

    private final Plugin plugin;

    public PluginModule(final Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        /* Bind default Bukkit API classes. */
        bind(Plugin.class).toInstance(this.plugin);
        bind(Server.class).toInstance(this.plugin.getServer());
        bind(File.class).annotatedWith(PluginFolder.class).toInstance(this.plugin.getDataFolder());
        bind(World.class).annotatedWith(DefaultWorld.class).toInstance(this.plugin.getServer().getWorlds().get(0));
        bind(ServicesManager.class).toInstance(this.plugin.getServer().getServicesManager());
        bind(PluginManager.class).toInstance(this.plugin.getServer().getPluginManager());
        bind(ScoreboardManager.class).toInstance(this.plugin.getServer().getScoreboardManager());
        bind(BukkitScheduler.class).toInstance(this.plugin.getServer().getScheduler());
        bind(Scoreboard.class).annotatedWith(MainScoreboard.class).toInstance(this.plugin.getServer().getScoreboardManager().getMainScoreboard());
        bind(ItemFactory.class).toInstance(this.plugin.getServer().getItemFactory());
        bindCommandMap();
    }

    private void bindCommandMap() {
        try {
            /* Get the command map from the server by using reflection. */
            final Field field = plugin.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            bind(CommandMap.class).toInstance((CommandMap) field.get(plugin.getServer()));
        } catch (Exception e) {
            throw new RuntimeException("Could not find command map.", e);
        }
    }

    @Provides
    public Scoreboard createScoreboard(ScoreboardManager manager) {
        return manager.getNewScoreboard();
    }
}
