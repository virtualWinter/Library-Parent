package me.vwinter.library.command;

import com.google.inject.Injector;
import com.google.inject.Key;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.node.CommandNode;
import me.vwinter.library.command.parametric.CommandParameter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.util.StringUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

public class BukkitBaseCommand<T> extends BukkitCommand {

    private final CommandNode<T> rootNode;
    private final Injector injector;

    public BukkitBaseCommand(final String name, final CommandNode<T> rootNode, final Injector injector) {
        super(name);
        this.rootNode = rootNode;
        this.injector = injector;

        CommandContext<T> context = rootNode.getContext();

        setAliases(Arrays.asList(context.getAliases()));

        setDescription(context.getDescription());
        setPermission(context.getPermission());
        setPermissionMessage(context.getPermissionMessage());
        setUsage("/" + context.getName() + " " + context.getUsage());
    }

    @Override
    public boolean execute(final CommandSender sender, final String alias, final String[] args) {
        CommandNode<T> currentNode = rootNode;
        int currentIndex = 0;

        findCurrent:
        for (String arg : args) {
            if (currentNode.getChildNodes().isEmpty()) {
                break;
            }

            for (CommandNode<T> childNode : currentNode.getChildNodes()) {
                for (String childAlias : childNode.getContext().getAliases()) {
                    if (childAlias.equalsIgnoreCase(arg)) {
                        currentNode = childNode;
                        currentIndex++;
                        continue findCurrent;
                    }
                }

                if (childNode.getContext().getName().equalsIgnoreCase(arg)) {
                    currentNode = childNode;
                    currentIndex++;
                    continue findCurrent;
                }
            }
        }

        if (currentNode.getChildNodes().isEmpty()) {
            int j = currentIndex;
            boolean hasArguments = false;
            for (int i = 0; i < currentNode.getContext().getParameters().size(); i++) {
                CommandParameter<?> parameter = currentNode.getContext().getParameters().get(i);
                if (!parameter.isArgument()) {
                    continue;
                }

                if (!parameter.isOptional()) {
                    hasArguments = true;
                }

                j++;

                if (j == args.length) {
                    runNode(sender, currentIndex, args, currentNode);
                    return true;
                }
            }

            if (j > args.length && hasArguments) {
                sendUsageMessage(sender, currentNode, true, args);
                return true;
            }

            runNode(sender, currentIndex, args, currentNode);
            return true;
        }

        sendUsageMessage(sender, currentNode, true, args);
        return true;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) {
        CommandNode<T> currentNode = rootNode;
        int currentIndex = 0;

        findCurrent:
        for (String arg : args) {
            if (currentNode.getChildNodes().isEmpty()) {
                break;
            }

            for (CommandNode<T> childNode : currentNode.getChildNodes()) {
                for (String childAlias : childNode.getContext().getAliases()) {
                    if (childAlias.equalsIgnoreCase(arg)) {
                        currentNode = childNode;
                        currentIndex++;
                        continue findCurrent;
                    }
                }

                if (childNode.getContext().getName().equalsIgnoreCase(arg)) {
                    currentNode = childNode;
                    currentIndex++;
                    continue findCurrent;
                }
            }
        }

        if (!sender.hasPermission(currentNode.getContext().getPermission())) {
            return Collections.emptyList();
        }

        String lastWord = args[args.length - 1];

        if (currentNode.getChildNodes().isEmpty()) {
            for (int i = 0, j = currentIndex; i < currentNode.getContext().getParameters().size(); i++) {
                CommandParameter<?> parameter = currentNode.getContext().getParameters().get(i);
                if (!parameter.isArgument()) {
                    continue;
                }

                j++;

                if (j == args.length) {
                    return parameter.getParser().getSuggestions(new WrappedSender(sender), currentNode.getContext(), lastWord);
                }
            }

            return injector.getInstance(CommandService.class)
                    .getParser(Key.get(String.class))
                    .getSuggestions(new WrappedSender(sender), currentNode.getContext(), lastWord);
        }

        return currentNode.getChildNodes()
                .stream()
                .map(node -> node.getContext().getName())
                .filter(name -> StringUtil.startsWithIgnoreCase(name, lastWord))
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
    }

    /* Send the usage message from any node to
     * a command sender. */
    private void sendUsageMessage(final CommandSender sender, final CommandNode<T> node, boolean runIfOverride, final String[] args) {
        if (!sender.hasPermission(node.getContext().getPermission())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', node.getContext().getPermissionMessage()));
            return;
        }

        if (runIfOverride) {
            if (node.isRootNode() && node.getContext().overridesUsage()) {
                runNode(sender, 0, args, node);
                return;
            }
        }

        List<CommandNode<T>> nodePath = getNodePath(node, new ArrayList<>());
        StringBuilder pathUsage = new StringBuilder();

        Collections.reverse(nodePath);

        for (int i = 0; i < nodePath.size(); i++) {
            CommandNode<T> commandNode = nodePath.get(i);
            pathUsage.append(commandNode.getContext().getName());
            if (i != nodePath.size() - 1) {
                pathUsage.append(" ");
            }
        }

        if (node.getChildNodes().isEmpty()) {
            sender.sendMessage(color("&cUsage: /" + pathUsage + " " + node.getContext().getUsage()));
            return;
        }

        final StringBuilder builder = new StringBuilder();
        builder.append("&cUsage for ").append(pathUsage).append(":\n");

        for (CommandNode<T> childNode : node.getChildNodes()) {
            builder.append("&c/")
                    .append(pathUsage)
                    .append(" ")
                    .append(childNode.getContext().getName())
                    .append(" ")
                    .append(childNode.getContext().getUsage())
                    .append("\n");
        }

        sender.sendMessage(color(builder.toString()));
    }


    private List<CommandNode<T>> getNodePath(CommandNode<T> node, List<CommandNode<T>> nodes) {
        nodes.add(node);

        if (node.getParentNode() != null) {
            return getNodePath(node.getParentNode(), nodes);
        }

        return nodes;
    }

    /* Run a command node */
    private void runNode(final CommandSender sender, int index, final String[] args, final CommandNode<T> node) {
        final CommandContext<T> context = node.getContext();

        if (!sender.hasPermission(context.getPermission())) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', context.getPermissionMessage()));
            return;
        }

        final Method method = node.getMethod();
        final Object[] arguments = new Object[method.getParameterCount()];

        /* Iterate through the contexts parameters and check
         * their state then append them to the arguments array. */
        for (int i = 0; i < context.getParameters().size(); i++) {
            final CommandParameter<?> parameter = context.getParameters().get(i);

            if (parameter.isInjected()) {
                arguments[i] = injector.getInstance(Key.get(parameter.getTypeLiteral()));
                continue;
            }

            /* If the parameter is a sender, we check if
             * it is an instance of CommandSender, if it
             * is, we cast it. Otherwise, we send the
             * invalid sender message. */
            if (parameter.isSender()) {
                Object paramSender = parameter.getSenderProvider().get(new WrappedSender(sender));

                if (paramSender == null) {
                    sender.sendMessage(color(parameter.getSenderProvider().getInvalidSenderMessage(new WrappedSender(sender))));
                    return;
                }

                arguments[i] = paramSender;
                continue;
            }

            /* If the parameter is text (String), we create
             * a stringbuilder and append all of the
             * remaining strings from the command arguments. */
            if (parameter.hasText()) {
                final StringBuilder builder = new StringBuilder();

                for (int j = index; j < args.length; j++) {
                    builder.append(args[j]);
                    builder.append(j == args.length - 1 ? "" : " ");
                }

                arguments[i] = builder.toString();
                continue;
            }

            try {
                final Object o = parameter.getParser().parse(new WrappedSender(sender), context, args[index]);

                if (o == null && !parameter.isOptional()) {
                    sender.sendMessage(color(parameter.getParser().getParseMessage(new WrappedSender(sender), args[index])));
                    return;
                }

                arguments[i] = o;
            } catch (Exception e) {
                if (!parameter.isOptional()) {
                    sendUsageMessage(sender, node, false, args);
                    return;
                }
            }

            if (parameter.isArgument()) {
                index++;
            }
        }

        /* Invoke the method using reflection with the built
         * object arguments array. */
        if (node.getContext().isAsync()) {
            ForkJoinPool.commonPool().execute(() -> {
                try {
                    method.invoke(node.getInstance(), arguments);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    sender.sendMessage(color(context.getExceptionMessage()));
                    e.printStackTrace();
                }
            });

            return;
        }

        try {
            method.invoke(node.getInstance(), arguments);
        } catch (IllegalAccessException | InvocationTargetException e) {
            sender.sendMessage(color(context.getExceptionMessage()));
            e.printStackTrace();
        }
    }

    private String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
