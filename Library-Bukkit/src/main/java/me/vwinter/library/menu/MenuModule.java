package me.vwinter.library.menu;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import me.vwinter.library.framework.annotation.PostInject;
import me.vwinter.library.menu.internal.InternalMenuHolderFactory;
import me.vwinter.library.menu.internal.InternalMenuHolderService;
import me.vwinter.library.storage.named.NamedHashStore;
import me.vwinter.library.storage.named.NamedStore;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class MenuModule extends AbstractModule {

    @Override
    protected void configure() {
        final NamedStore<MenuHolder> store = new NamedHashStore<>();
        bind(new TypeLiteral<NamedStore<MenuHolder>>() {
        }).toInstance(store);
        bind(MenuHolderFactory.class).toInstance(new InternalMenuHolderFactory(getProvider(Injector.class), store));
        bind(MenuHolderService.class).toInstance(new InternalMenuHolderService(store));
    }

    @PostInject
    public void registerListeners(final Plugin plugin, final PluginManager manager, final MenuListener listener) {
        manager.registerEvents(listener, plugin);
    }

    @PostInject
    public void startTask(final Plugin plugin, final BukkitScheduler scheduler, final MenuRunnable runnable) {
        scheduler.runTaskTimerAsynchronously(plugin, runnable, 2L, 2L);
    }
}
