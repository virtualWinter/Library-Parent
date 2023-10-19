package me.vwinter.library.command;

import com.google.inject.AbstractModule;
import com.google.inject.util.Providers;
import me.vwinter.library.command.internal.InternalCommandService;

public abstract class AbstractCommandModule extends AbstractModule {

    public abstract AbstractCommandRegistrar getRegistrar();

    public abstract void registerDefaults(final CommandService service);

    @Override
    protected void configure() {
        AbstractCommandRegistrar registrar = getRegistrar();
        bind(AbstractCommandRegistrar.class).toInstance(registrar);

        CommandService service = new InternalCommandService(Providers.of(registrar));
        bind(CommandService.class).toInstance(service);

        registerDefaults(service);
    }
}
