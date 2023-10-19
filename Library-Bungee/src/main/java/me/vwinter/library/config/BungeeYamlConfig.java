package me.vwinter.library.config;

import me.vwinter.library.concurrent.Callback;
import me.vwinter.library.framework.Project;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class BungeeYamlConfig implements Config {

    private final File file;
    private final ConfigurationProvider provider;
    private final Configuration wrapped;
    private final ExecutorService executorService;

    public BungeeYamlConfig(final String path, ExecutorService executorService) {
        this(new File(path), executorService);
    }

    public BungeeYamlConfig(final Project project, final String path, ExecutorService executorService) {
        this.executorService = executorService;

        try {
            InputStream inputStream = project.getResource(path);
            this.file = new File(project.getProjectFolder(), path);

            if (file.exists()) {
                inputStream = Files.newInputStream(file.toPath());
            }

            this.provider = YamlConfiguration.getProvider(YamlConfiguration.class);
            this.wrapped = this.provider.load(inputStream);
            this.provider.save(this.wrapped, file);
        } catch (final IOException e) {
           throw new RuntimeException("Could not load config.", e);
        }
    }

    public BungeeYamlConfig(final File file, ExecutorService executorService) {
        this.file = file;
        this.executorService = executorService;
        this.provider = YamlConfiguration.getProvider(YamlConfiguration.class);

        try {
            this.wrapped = this.provider.load(file);
            this.provider.save(this.wrapped, file);
        } catch (final IOException e) {
            throw new RuntimeException("Could not load config.", e);
        }
    }

    @Override
    public File getFile() {
        return this.file;
    }

    @Override
    public <T> T get(final String path) {
        if (!wrapped.contains(path)) {
            return null;
        }

        return (T) wrapped.get(path);
    }

    @Override
    public List<String> getList(final String path) {
        return get(path);
    }

    @Override
    public <T> void set(final String path, final T t) {
        wrapped.set(path, t);
    }

    @Override
    public void reload() {
        try {
           this.provider.load(file, wrapped);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void reloadAsync(final Callback callback) {
        this.executorService.submit(() -> {
            reload();
            callback.call();
        });
    }

    @Override
    public void save() {
        try {
            this.provider.save(this.wrapped, file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAsync(final Callback callback) {
        this.executorService.submit(() -> {
            save();
            callback.call();
        });
    }

    @Override
    public void delete() {
        file.delete();
    }
}
