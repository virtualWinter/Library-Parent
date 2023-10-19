package me.vwinter.library.sidebar;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import me.vwinter.library.framework.annotation.PostInject;
import me.vwinter.library.sidebar.internal.InternalSidebarFactory;
import me.vwinter.library.sidebar.internal.InternalSidebarService;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class SidebarModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(SidebarProvider.class).to(DefaultSidebarProvider.class).in(Scopes.SINGLETON);
        bind(SidebarFactory.class).to(InternalSidebarFactory.class);
        bind(SidebarService.class).to(InternalSidebarService.class).in(Scopes.SINGLETON);
    }


    @PostInject
    public void registerListeners(final Plugin plugin, final PluginManager manager, final SidebarListener listener) {
        manager.registerEvents(listener, plugin);
    }

    @PostInject
    public void startTask(final Plugin plugin, final BukkitScheduler scheduler, final SidebarRunnable runnable) {
        scheduler.runTaskTimerAsynchronously(plugin, runnable, 2L, 2L);
    }
}
