package me.vwinter.library.command.internal;

import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.parametric.CommandParameter;

import java.util.List;

class InternalCommandContext<T> implements CommandContext<T> {

    private final String fallbackPrefix, name, permission, permissionMessage, description, exceptionMessage;
    private final String[] aliases;
    private final boolean async, overrideUsage;
    private final List<CommandParameter<?>> parameters;
    private final String usage;

    InternalCommandContext(final String fallbackPrefix, final String name, final String permission, final String permissionMessage, final String description, String exceptionMessage, final String[] aliases, final boolean async, boolean overrideUsage, final List<CommandParameter<?>> parameters, final CommandContext<T> parent) {
        this.fallbackPrefix = fallbackPrefix;
        this.name = name;
        this.permission = permission;
        this.permissionMessage = permissionMessage;
        this.description = description;
        this.exceptionMessage = exceptionMessage;
        this.aliases = aliases;
        this.async = async;
        this.overrideUsage = overrideUsage;
        this.parameters = parameters;

        /* Here we build the usage for this context.
         * This is how they will be parsed:
         *   - [] are optional.
         *   - <> are required.
         *   - ... are text. */
        final StringBuilder usageBuilder = new StringBuilder();

        boolean hasArguments = false;
        for (int i = 0; i < parameters.size(); i++) {
            final CommandParameter<?> parameter = parameters.get(i);
            if (!parameter.isArgument()) continue;
            hasArguments = true;

            if (parameter.isOptional()) {
                usageBuilder.append("[");
                usageBuilder.append(parameter.getName());

                if (parameter.hasText()) {
                    usageBuilder.append("...");
                }

                usageBuilder.append("]");
                continue;
            }

            usageBuilder.append("<");
            usageBuilder.append(parameter.getName());

            if (parameter.hasText()) {
                usageBuilder.append("...");
            }

            usageBuilder.append(">");
            usageBuilder.append(i == parameters.size() - 1 ? "" : " ");
        }

        this.usage = (hasArguments ? usageBuilder.toString() : "");
    }

    @Override
    public String getFallbackPrefix() {
        return this.fallbackPrefix;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String[] getAliases() {
        return this.aliases;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public String getPermissionMessage() {
        return this.permissionMessage;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String getExceptionMessage() {
        return this.exceptionMessage;
    }

    @Override
    public String getUsage() {
        return this.usage;
    }

    @Override
    public boolean isAsync() {
        return this.async;
    }

    @Override
    public boolean overridesUsage() {
        return this.overrideUsage;
    }

    @Override
    public List<CommandParameter<?>> getParameters() {
        return this.parameters;
    }

}
