package me.vwinter.library.config;

import com.google.common.base.Charsets;
import me.vwinter.library.concurrent.Callback;
import me.vwinter.library.framework.BukkitProject;
import me.vwinter.library.framework.Project;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class BukkitYamlConfig implements Config {

    private final File file;
    private final YamlConfiguration wrapped;
    private final ExecutorService executorService;

    public BukkitYamlConfig(final String path, ExecutorService executorService) {
        this(new File(path), executorService);
    }

    public BukkitYamlConfig(final Project project, final String path, ExecutorService executorService) {
        this.executorService = executorService;

        ((BukkitProject) project).saveResource(path, false);

        try {
            this.file = new File(project.getProjectFolder(), path);
            final InputStream inputStream = Files.newInputStream(file.toPath());
            this.wrapped = YamlConfiguration.loadConfiguration(new InputStreamReader(inputStream, Charsets.UTF_8));
            this.wrapped.save(file);
        } catch (final IOException e) {
            throw new RuntimeException("Could not load config " + path, e);
        }
    }

    public BukkitYamlConfig(final File file, ExecutorService executorService) {
        this.file = file;
        this.executorService = executorService;
        this.wrapped = new YamlConfiguration();
        try {
            this.wrapped.load(file);
            this.wrapped.save(file);
        } catch (final IOException | InvalidConfigurationException e) {
            e.printStackTrace();
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
            wrapped.load(file);
        } catch (final IOException | InvalidConfigurationException e) {
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
            wrapped.save(file);
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
