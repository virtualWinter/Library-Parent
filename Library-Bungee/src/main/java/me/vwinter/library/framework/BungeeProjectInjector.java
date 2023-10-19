package me.vwinter.library.framework;

import com.google.inject.Injector;
import com.google.inject.Module;
import me.vwinter.library.framework.plugin.PluginModule;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BungeeProjectInjector implements ProjectInjector<BungeeProject> {

    private Framework<BungeeProject> framework;
    private final Map<BungeeProject, Injector> map;

    public BungeeProjectInjector() {
        this.map = new ConcurrentHashMap<>();
    }

    protected final void setup(final Framework<BungeeProject> framework) {
        this.framework = framework;
    }

    @Override
    public Framework<BungeeProject> getFramework() {
        return this.framework;
    }

    @Override
    public Injector createInjector(final BungeeProject project, Module... modules) {
        return createInjector(project, Arrays.asList(modules));
    }

    @Override
    public Injector createInjector(final BungeeProject project, final Iterable<Module> modules) {
        Injector injector = framework.getInjector().createChildInjector(new PluginModule(project));
        map.put(project, injector);
        getFramework().getProjects().add(project);
        return injector;
    }

    @Override
    public Injector getInjector(final BungeeProject project) {
        return map.get(project);
    }
}
