package me.vwinter.library.framework;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

public class BungeeFramework implements Framework<BungeeProject> {

    private static final BungeeFramework INSTANCE;

    static {
        final BungeeProjectInjector INJECTOR = new BungeeProjectInjector();
        INSTANCE = new BungeeFramework(INJECTOR);
        INJECTOR.setup(INSTANCE);
    }

    public static BungeeFramework getInstance() {
        return INSTANCE;
    }

    private final Injector injector;
    private final Set<BungeeProject> projects;
    private final ProjectInjector<BungeeProject> projectInjector;

    public BungeeFramework(final Injector injector, final ProjectInjector<BungeeProject> projectInjector) {
        this.injector = injector;
        this.projectInjector = projectInjector;
        this.projects = Sets.newConcurrentHashSet();
    }

    public BungeeFramework(final ProjectInjector<BungeeProject> projectInjector, Module... modules) {
        this(Arrays.asList(modules), projectInjector);
    }

    public BungeeFramework(final Iterable<Module> modules, final ProjectInjector<BungeeProject> projectInjector) {
        this.injector = Guice.createInjector(Stage.DEVELOPMENT, modules);
        this.projectInjector = projectInjector;
        this.projects = Sets.newConcurrentHashSet();
    }

    @Override
    public Injector getInjector() {
        return this.injector;
    }

    @Override
    public Set<BungeeProject> getProjects() {
        return this.projects;
    }

    public <T extends BungeeProject> T getProjectByPluginName(String name) {
        return getProjectBy(project -> project.getName().equalsIgnoreCase(name));
    }

    public <T extends BungeeProject> T getProjectBy(Predicate<? super T> predicate) {
        for (BungeeProject project : getProjects()) {
            if (predicate.test((T) project)) {
                return (T) project;
            }
        }

        return null;
    }

    @Override
    public void configure(BungeeProject parentProject, BungeeProject project) {
        project.setup(parentProject, projectInjector);
    }

}
