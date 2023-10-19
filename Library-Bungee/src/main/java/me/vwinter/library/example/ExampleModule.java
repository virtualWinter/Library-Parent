package me.vwinter.library.example;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import me.vwinter.library.command.CommandService;
import me.vwinter.library.framework.annotation.PostInject;

public class ExampleModule extends AbstractModule {

    @PostInject
    public void registerCommands(final CommandService service, final Injector injector) {
        service.registerNodes(ExampleCommands.class, injector);
    }

}
