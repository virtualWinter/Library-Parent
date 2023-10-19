package me.vwinter.library.framework;

import com.google.common.collect.Sets;
import com.google.common.reflect.TypeResolver;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import me.vwinter.library.framework.annotation.PostInject;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;
import java.util.Set;

public abstract class BungeeProject extends Plugin implements Project {

    private boolean configured;
    private boolean root;
    private BungeeProject parent;
    private final Set<AbstractModule> modules;
    private Injector injector;
    private BungeeFramework framework;

    public BungeeProject() {
        this.modules = Sets.newConcurrentHashSet();
    }

    public abstract Optional<String> getParentPluginName();

    @Override
    public final void onEnable() {
        if (!this.configured) {
            BungeeFramework framework = BungeeFramework.getInstance();

            if (getParentPluginName() != null && getParentPluginName().isPresent()) {
                BungeeProject projectParent = framework.getProjectByPluginName(getParentPluginName().get());

                framework.configure(projectParent, this);
            } else {
                framework.configure(this);
            }
        }

        enable();
    }

    @Override
    public final void onDisable() {
        disable();
        this.configured = false;
    }

    public synchronized void setup(final BungeeProject parent, final ProjectInjector<BungeeProject> injector) {
        this.framework = (BungeeFramework) injector.getFramework();

        if (parent != null) {
            this.parent = parent;
            this.root = false;
        } else {
            this.root = true;
        }

        if (this.configured) {
            return;
        }

        final Injector parentInjector = injector.createInjector(this);
        this.injector = parentInjector;
        this.configure();
        this.configured = true;
        this.injector = parentInjector.createChildInjector(this.modules);

        /* Iterate through modules and search for the
         * PostInject annotation. If it is
         * present, invoke the method with the
         * injector, injecting all of its parameters.
         *
         * This is primarily used to allow modules to
         * instantiate objects without needing to
         * reference the injector statically. Which
         * can also be very hard because the parent
         * injector is in the 'head' as the modules
         * are configured. */
        modules.forEach(module -> {
            for (final Method method : module.getClass().getDeclaredMethods()) {
                method.setAccessible(true);

                /* Check if the PostInject annotation is present. */
                if (method.isAnnotationPresent(PostInject.class)) {
                    final Object[] arguments = new Object[method.getParameterCount()];

                    for (int i = 0; i < method.getParameterCount(); i++) {
                        final Parameter parameter = method.getParameters()[i];

                        /* Resolve the type and append the injected parameter. */
                        final Class<?> typeClass = TypeLiteral.get(
                                new TypeResolver().resolveType(parameter.getType())).getRawType();

                        arguments[i] = this.injector.getInstance(typeClass);
                    }

                    try {
                        method.invoke(module, arguments);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new RuntimeException("Could not inject method annotated with PostInject.", e);
                    }
                }
            }

            /* Finally, inject members. */
            this.injector.injectMembers(module);
        });
    }

    public String getName() {
        return getDescription().getName();
    }

    @Override
    public BungeeFramework getFramework() {
        return framework;
    }

    @Override
    public boolean isRootProject() {
        return this.root;
    }

    @Override
    public Optional<BungeeProject> getParent() {
        return Optional.ofNullable(this.parent);
    }

    @Override
    public void configure() {
    }

    @Override
    public void enable() {
    }

    @Override
    public void disable() {
    }

    @Override
    public File getProjectFolder() {
        return getDataFolder();
    }

    @Override
    public boolean isConfigured() {
        return this.configured;
    }

    @Override
    public <T extends AbstractModule> void install(final T t) {
        if (this.configured) {
            throw new RuntimeException("Project has already been configured.");
        }

        this.modules.add(t);
    }

    @Override
    public InputStream getResource(String path) {
        return getResourceAsStream(path);
    }

    @Override
    public Injector getInjector() {
        return this.injector;
    }
}
