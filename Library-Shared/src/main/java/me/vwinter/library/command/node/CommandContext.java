package me.vwinter.library.command.node;

import me.vwinter.library.command.parametric.CommandParameter;

import java.util.List;

public interface CommandContext<T> {

    /**
     * Returns the fallback prefix.
     */
    String getFallbackPrefix();

    /**
     * Returns the name.
     */
    String getName();

    /**
     * Returns an array of aliases.
     */
    String[] getAliases();

    /**
     * Returns the permission.
     */
    String getPermission();

    /**
     * Returns the permission message.
     */
    String getPermissionMessage();

    /**
     * Returns the description.
     */
    String getDescription();

    /**
     * Returns the exception message.
     */
    String getExceptionMessage();

    /**
     * Returns the usage.
     */
    String getUsage();

    /**
     * Returns a boolean whether this command is executed
     * asynchronously or not.
     */
    boolean isAsync();

    /**
     * Returns a boolean whether this command should
     * override the default usage or not.
     */
    boolean overridesUsage();

    /**
     * Returns a list of parameters.
     */
    List<CommandParameter<?>> getParameters();

}
