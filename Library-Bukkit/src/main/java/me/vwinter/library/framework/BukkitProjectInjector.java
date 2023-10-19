package me.vwinter.library.framework;

import com.google.inject.Injector;
import com.google.inject.Module;
import me.vwinter.library.framework.plugin.PluginModule;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BukkitProjectInjector implements ProjectInjector<BukkitProject> {

    private Framework<BukkitProject> framework;
    private final Map<BukkitProject, Injector> map;

    public BukkitProjectInjector() {
        this.map = new ConcurrentHashMap<>();
    }

    protected final void setup(final Framework<BukkitProject> framework) {
        this.framework = framework;
    }

    @Override
    public Framework<BukkitProject> getFramework() {
        return this.framework;
    }

    @Override
    public Injector createInjector(final BukkitProject project, Module... modules) {
        return createInjector(project, Arrays.asList(modules));
    }

    @Override
    public Injector createInjector(final BukkitProject project, final Iterable<Module> modules) {
        Injector injector = framework.getInjector().createChildInjector(new PluginModule(project));
        map.put(project, injector);
        getFramework().getProjects().add(project);
        return injector;
    }

    @Override
    public Injector getInjector(final BukkitProject project) {
        return map.get(project);
    }
}
