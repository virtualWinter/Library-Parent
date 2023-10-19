package me.vwinter.library.framework;

import com.google.common.collect.Sets;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;

import java.util.Arrays;
import java.util.Set;
import java.util.function.Predicate;

public class BukkitFramework implements Framework<BukkitProject> {

    private static final BukkitFramework INSTANCE;

    static {
        final BukkitProjectInjector INJECTOR = new BukkitProjectInjector();
        INSTANCE = new BukkitFramework(INJECTOR);
        INJECTOR.setup(INSTANCE);
    }

    public static BukkitFramework getInstance() {
        return INSTANCE;
    }

    private final Injector injector;
    private final Set<BukkitProject> projects;
    private final ProjectInjector<BukkitProject> projectInjector;

    public BukkitFramework(final Injector injector, final ProjectInjector<BukkitProject> projectInjector) {
        this.injector = injector;
        this.projectInjector = projectInjector;
        this.projects = Sets.newConcurrentHashSet();
    }

    public BukkitFramework(final ProjectInjector<BukkitProject> projectInjector, Module... modules) {
        this(Arrays.asList(modules), projectInjector);
    }

    public BukkitFramework(final Iterable<Module> modules, final ProjectInjector<BukkitProject> projectInjector) {
        this.injector = Guice.createInjector(Stage.DEVELOPMENT, modules);
        this.projectInjector = projectInjector;
        this.projects = Sets.newConcurrentHashSet();
    }

    @Override
    public Injector getInjector() {
        return this.injector;
    }

    @Override
    public Set<BukkitProject> getProjects() {
        return this.projects;
    }

    public <T extends BukkitProject> T getProjectByPluginName(final String name) {
        return getProjectBy(project -> project.getName().equalsIgnoreCase(name));
    }

    public <T extends BukkitProject> T getProjectBy(final Predicate<? super T> predicate) {
        for (BukkitProject project : getProjects()) {
            if (predicate.test((T) project)) {
                return (T) project;
            }
        }

        return null;
    }

    @Override
    public void configure(final BukkitProject parentProject, final BukkitProject project) {
        project.setup(parentProject, projectInjector);
    }

}
