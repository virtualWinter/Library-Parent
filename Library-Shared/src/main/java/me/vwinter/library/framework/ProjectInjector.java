package me.vwinter.library.framework;

import com.google.inject.Injector;
import com.google.inject.Module;

public interface ProjectInjector<T extends Project> {

    /**
     * Returns this project-injectors framework.
     */
    Framework<T> getFramework();

    /**
     * Returns a new injector for a project with modules.
     */
    Injector createInjector(final T project, Module... modules);

    /**
     * Returns a new injector for a project with modules.
     */
    Injector createInjector(final T project, final Iterable<Module> modules);

    /**
     * Returns the projects injector.
     */
    Injector getInjector(final T project);

}
