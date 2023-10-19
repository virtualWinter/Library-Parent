package me.vwinter.library.nametag;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.vwinter.library.framework.annotation.PostInject;
import me.vwinter.library.nametag.internal.InternalNameTagFactory;
import me.vwinter.library.nametag.internal.InternalNameTagService;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class NameTagModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(NameTagProvider.class).to(DefaultNameTagProvider.class).in(Scopes.SINGLETON);
        bind(NameTagFactory.class).to(InternalNameTagFactory.class);
        bind(NameTagService.class).to(InternalNameTagService.class).in(Scopes.SINGLETON);
    }


    @PostInject
    public void registerListeners(final Plugin plugin, final PluginManager manager, final NameTagListener listener) {
        manager.registerEvents(listener, plugin);
    }

    @PostInject
    public void startTask(final Plugin plugin, final BukkitScheduler scheduler, final NameTagRunnable runnable) {
        scheduler.runTaskTimerAsynchronously(plugin, runnable, 2L, 2L);
    }
}
