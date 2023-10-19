package me.vwinter.library.framework;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;

import java.io.File;
import java.io.InputStream;
import java.util.Optional;

public interface Project {

    /**
     * Returns this projects framework.
     */
    <T extends Project> Framework<T> getFramework();

    /**
     * Returns a boolean whether this project is a root project or not.
     */
    boolean isRootProject();

    /**
     * Returns this projects parent in an optional.
     */
    <T extends Project> Optional<T> getParent();

    /**
     * Configure this project.
     */
    void configure();

    /**
     * Enable this project.
     */
    void enable();

    /**
     * Disable this project.
     */
    void disable();

    /**
     * Check whether this project has been configured or not.
     */
    boolean isConfigured();

    /**
     * Returns the projects data folder.
     */
    File getProjectFolder();

    /**
     * Install a module for this project.
     */
    <T extends AbstractModule> void install(final T t);

    /**
     * Returns this projects injector.
     */
    Injector getInjector();

    /**
     * Returns the projects class-loaders input-stream of the path.
     */
    InputStream getResource(String path);
}
