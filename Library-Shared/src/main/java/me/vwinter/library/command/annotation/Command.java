package me.vwinter.library.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Command {

    /**
     * Defaults for the command interface values.
     */
    final class Defaults {
        public static final String FALLBACK_PREFIX = "library";

        public static final String PERMISSION = "";

        public static final String PERMISSION_MESSAGE = "I'm sorry but you don't have permission to perform this command.";

        public static final boolean ASYNC = false;

        public static final String DESCRIPTION = "Undefined.";

        public static final String EXCEPTION_MESSAGE = "We are sorry but there has been an error executing your command.";

        public static final boolean USAGE = false;
    }

    /**
     * Returns the default fallback prefix used in the command map.
     */
    String fallbackPrefix() default Defaults.FALLBACK_PREFIX;

    /**
     * Returns an array or a single string of command alias(es).
     */
    String[] aliases();

    /**
     * Returns the command's description.
     */
    String description() default Defaults.DESCRIPTION;

    /**
     * Returns the command's permission.
     */
    String permission() default Defaults.PERMISSION;

    /**
     * Returns the command's permission message.
     */
    String permissionMessage() default Defaults.PERMISSION_MESSAGE;

    /**
     * Returns whether the command should be executed asynchronously or not.
     */
    boolean async() default Defaults.ASYNC;

    /**
     * Returns the command's exception message.
     */
    String exceptionMessage() default Defaults.EXCEPTION_MESSAGE;

    /**
     * Returns whether the command should override the usage or not.
     */
    boolean usage() default Defaults.USAGE;

}
