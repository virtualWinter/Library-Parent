package me.vwinter.library.command.internal;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.reflect.TypeResolver;
import com.google.inject.*;
import me.vwinter.library.command.AbstractCommandRegistrar;
import me.vwinter.library.command.CommandService;
import me.vwinter.library.command.annotation.*;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.node.CommandNode;
import me.vwinter.library.command.parametric.CommandParameter;
import me.vwinter.library.command.parametric.argument.ArgumentParser;
import me.vwinter.library.command.parametric.defaults.BooleanParser;
import me.vwinter.library.command.parametric.defaults.FloatParser;
import me.vwinter.library.command.parametric.defaults.IntegerParser;
import me.vwinter.library.command.parametric.defaults.LongParser;
import me.vwinter.library.command.parametric.provider.CommandSenderProvider;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class InternalCommandService implements CommandService {

    private final Map<Key<?>, ArgumentParser<?>> parserMap;
    private final Map<Key<?>, CommandSenderProvider<?>> providerMap;
    private final Map<String, CommandNode<?>> nodeMap;
    private final Provider<AbstractCommandRegistrar> registrarProvider;

    @Inject
    public InternalCommandService(final Provider<AbstractCommandRegistrar> registrarProvider) {
        this.registrarProvider = registrarProvider;
        this.parserMap = Maps.newConcurrentMap();
        this.providerMap = Maps.newConcurrentMap();
        this.nodeMap = Maps.newConcurrentMap();

        /* Register the default parsers. */
        registerParsers(new BooleanParser(), Boolean.class, boolean.class);
        registerParsers(new IntegerParser(), Integer.class, int.class);
        registerParsers(new FloatParser(), Float.class, float.class);
        registerParsers(new LongParser(), Long.class, long.class);
    }

    @SafeVarargs
    private final <T> void registerParsers(final ArgumentParser<T> parser, final Class<? extends T>... types) {
        registerParsers(parser, Arrays.asList(types));
    }

    /* Registers the classes */
    private <T> void registerParsers(final ArgumentParser<T> parser, final Iterable<Class<? extends T>> iterable) {
        iterable.forEach(aClass -> registerParser(Key.get(aClass), parser));
    }

    @Override
    public synchronized <T> void registerNodes(final Class<? extends T> aClass, Injector injector) {
        T instance = injector.getInstance(aClass);

        /* Iterate through the methods in the class and check if
         * they have the Command annotation. If they do, we parse
         * the methods parameters and create command parameters.
         *
         * We then try and find the parent node by the first
         * alias, if it is found, we create a child node from
         * that. We then create the command context from the
         * parameters and command meta.
         *
         * All the root nodes will be cached in a map.
         * After that we register the command to bukkit's
         * command map. */

        Arrays.stream(aClass.getDeclaredMethods()).filter(method -> {
            method.setAccessible(true); return method.isAnnotationPresent(Command.class);
        }).sorted(Comparator.comparingInt(method -> {
            final Command command = method.getAnnotation(Command.class);
            return command.aliases()[0].split(" ").length;
        })).forEach(method -> registerNode(aClass, instance, method));

        AbstractCommandRegistrar registrar = registrarProvider.get();

        for (Map.Entry<String, CommandNode<?>> entry : nodeMap.entrySet()) {
            registrar.registerCommand(entry.getValue(), injector);
        }
    }

    private <T> void registerNode(Class<? extends T> aClass, T instance, Method method) {
        final Command command = method.getAnnotation(Command.class);
        if (command == null) return;

        final String[] names = command.aliases()[0].split(" ");
        final String[] aliases = Arrays.copyOfRange(command.aliases(), 1, command.aliases().length);

        CommandNode<T> parentNode = null;

        for (int i = 0; i < names.length; i++) {
            final String currentName = names[i];
            final List<String> currentAliasesList = Lists.newArrayList();

            for (String alias : aliases) {
                currentAliasesList.add(alias.split(" ")[i]);
            }

            final String[] currentAliases = currentAliasesList.toArray(new String[]{});

            if (parentNode == null) {
                CommandNode<?> currentNode = nodeMap.get(currentName);

                if (currentNode != null) {
                    parentNode = (CommandNode<T>) currentNode;
                } else {
                    parentNode = createNode(true, Lists.newArrayList(), null, instance, aClass, names.length == 1 ? method : null, currentName, currentAliases, command);
                    nodeMap.put(currentName, parentNode);
                }
                continue;
            }

            CommandNode<T> commandNode = createNode(false, Lists.newArrayList(), parentNode, instance, aClass, method, currentName, currentAliases, command);
            parentNode.getChildNodes().add(commandNode);
            parentNode = commandNode;
        }
    }

    private <T> CommandNode<T> createNode(boolean root, List<CommandNode<T>> childNodes, CommandNode<T> parent, T instance, Class<? extends T> aClass, Method method, String currentName, String[] currentAliases, Command command) {
        CommandContext<T> commandContext;
        List<CommandParameter<T>> parameters = Lists.newArrayList();

        if (method == null) {
            commandContext = new InternalCommandContext<>(
                    Command.Defaults.FALLBACK_PREFIX,
                    currentName,
                    Command.Defaults.PERMISSION,
                    Command.Defaults.PERMISSION_MESSAGE,
                    Command.Defaults.DESCRIPTION,
                    Command.Defaults.EXCEPTION_MESSAGE,
                    currentAliases,
                    Command.Defaults.ASYNC,
                    Command.Defaults.USAGE,
                    Lists.newArrayList()
                    , parent == null ? null : parent.getContext()
            );
        } else {
            /* Parsing the parameters */
            for (final Parameter parameter : method.getParameters()) {
                final boolean optional = parameter.isAnnotationPresent(Optional.class);
                final boolean sender = parameter.isAnnotationPresent(Sender.class);
                final boolean injected = parameter.isAnnotationPresent(Injected.class);
                final boolean text = parameter.isAnnotationPresent(Text.class);
                final boolean argument = parameter.isAnnotationPresent(Argument.class);
                String name = "undefined";

                if (parameter.isAnnotationPresent(Argument.class)) {
                    name = parameter.getAnnotation(Argument.class).value();
                }

                final TypeLiteral<?> typeLiteral = TypeLiteral.get(
                        new TypeResolver().resolveType(parameter.getType()));

                final ArgumentParser<?> argumentParser = this.getParser(Key.get(typeLiteral));
                final CommandSenderProvider<?> provider = this.getProvider(Key.get(typeLiteral));

                parameters.add(new InternalCommandParameter(
                        typeLiteral, argumentParser, provider, name, argument, optional, injected, sender, text));
            }

            commandContext = new InternalCommandContext(
                    command.fallbackPrefix(), currentName,
                    command.permission().equals(Command.Defaults.PERMISSION) && parent != null ? parent.getContext().getPermission() : command.permission(),
                    command.permissionMessage().equals(Command.Defaults.PERMISSION) && parent != null  ? parent.getContext().getPermissionMessage() : command.permissionMessage(),
                    command.description(), command.exceptionMessage(), currentAliases, command.async(), command.usage(), parameters,
                    parent == null ? null : parent.getContext());
        }

        return new InternalCommandNode(root, childNodes, parent, commandContext, instance, aClass, method);
    }

    @Override
    public <T> void registerParser(final Key<? extends T> key, final ArgumentParser<T> parser) {
        parserMap.put(key, parser);
    }

    @Override
    public <T> ArgumentParser<T> getParser(final Key<? extends T> key) {
        return (ArgumentParser<T>) parserMap.get(key);
    }

    @Override
    public <T> void registerProvider(final Key<? extends T> key, final CommandSenderProvider<T> provider) {
        providerMap.put(key, provider);
    }

    @Override
    public <T> CommandSenderProvider<T> getProvider(final Key<? extends T> key) {
        return (CommandSenderProvider<T>) providerMap.get(key);
    }
}
