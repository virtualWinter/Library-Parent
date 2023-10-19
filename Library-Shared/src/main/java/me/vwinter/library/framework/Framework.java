package me.vwinter.library.framework;

import com.google.inject.Injector;

import java.util.Set;
import java.util.function.Predicate;

public interface Framework<T extends Project> {

    /**
     * Returns the base injector.
     */
    Injector getInjector();

    /**
     * Returns a set of configured projects.
     */
    Set<T> getProjects();

    /**
     * Returns a project by a predicate.
     */
    <P extends T> P getProjectBy(final Predicate<? super P> predicate);

    /**
     * Configure a project without a parent.
     */
    default void configure(final T project) {
        configure(null, project);
    }

    /**
     * Configure a project with a parent.
     */
    void configure(final T parentProject, T project);

}
