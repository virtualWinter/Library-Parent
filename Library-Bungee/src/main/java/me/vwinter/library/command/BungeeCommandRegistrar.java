package me.vwinter.library.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.node.CommandNode;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

public class BungeeCommandRegistrar extends AbstractCommandRegistrar {

    private final Map<String, Command> commandMap;

    @Inject
    public BungeeCommandRegistrar(PluginManager pluginManager) {

        try {
            Field field = pluginManager.getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            this.commandMap = (Map<String, Command>) field.get(pluginManager);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException("Could not find Command Map", e);
        }
    }

    @Override
    public <T> void registerCommand(CommandNode<T> node, Injector injector) {
        CommandContext<T> context = node.getContext();
        Command command = new BungeeBaseCommand<>(context.getName(), node, injector);

        commandMap.put(context.getName(), command);

        String[] aliases = command.getAliases();
        int length = aliases.length;

        for (String alias : aliases) {
            this.commandMap.put(alias.toLowerCase(Locale.ROOT), command);
        }
    }
}
