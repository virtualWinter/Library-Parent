package me.vwinter.library.menu.internal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import me.vwinter.library.menu.*;
import me.vwinter.library.menu.annotation.MenuMeta;
import me.vwinter.library.storage.named.NamedStore;
import org.bukkit.ChatColor;

import java.util.Arrays;
import java.util.UUID;

public class InternalMenuHolderFactory implements MenuHolderFactory {

    private final Provider<Injector> injectorProvider;
    private final NamedStore<MenuHolder> store;

    @Inject
    public InternalMenuHolderFactory(final Provider<Injector> injectorProvider, final NamedStore<MenuHolder> store) {
        this.injectorProvider = injectorProvider;
        this.store = store;
    }

    @Override
    public <M extends Menu> MenuHolder createMenu(final Class<? extends M> menuClass) {
        final M menu = injectorProvider.get().getInstance(menuClass);
        final MenuMeta meta = menuClass.getAnnotation(MenuMeta.class);
        assert meta != null : "Menu is not annotated with MenuMeta.";

        String[] copiedAliases;

        if (meta.aliases().length > 1) {
            copiedAliases = Arrays.copyOfRange(meta.aliases(), 1, meta.aliases().length);
        } else {
            copiedAliases = new String[]{""};
        }

        final MenuContext context = new MenuContextBuilder().named(meta.aliases()[0]).withAliases(copiedAliases).withRows(meta.rows()).titled(ChatColor.translateAlternateColorCodes('&', meta.title())).build();

        final MenuHolder holder = new InternalMenuHolder(context, menu, UUID.randomUUID(), meta.aliases()[0]);
        store.add(holder);

        return holder;
    }

    @Override
    public <M extends Menu> MenuHolder createMenu(final M menu, final MenuContext context) {
        final MenuHolder holder = new InternalMenuHolder(context, menu, UUID.randomUUID(), context.getName());
        store.add(holder);
        return holder;
    }
}
