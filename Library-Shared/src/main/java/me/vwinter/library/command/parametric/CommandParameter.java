package me.vwinter.library.command.parametric;

import com.google.inject.TypeLiteral;
import me.vwinter.library.command.parametric.argument.ArgumentParser;
import me.vwinter.library.command.parametric.provider.CommandSenderProvider;

public interface CommandParameter<T> {

    /**
     * Returns this parameters generic type class. See {@link TypeLiteral#getType()}
     */
    TypeLiteral<T> getTypeLiteral();

    /**
     * Returns the parser for these parameters generic type.
     */
    ArgumentParser<T> getParser();

    /**
     * Returns the sender provider for these parameters generic type.
     */
    CommandSenderProvider<T> getSenderProvider();

    /**
     * Returns the name for this parameter.
     */
    String getName();

    /**
     * Returns a boolean whether this parameter is a defaults or not.
     */
    boolean isArgument();

    /**
     * Returns a boolean whether this parameter is optional or not.
     */
    boolean isOptional();

    /**
     * Returns a boolean whether this parameter is injected or not.
     */
    boolean isInjected();

    /**
     * Returns a boolean whether this parameter is a sender or not.
     */
    boolean isSender();

    /**
     * Returns a boolean whether this parameter has text or not.
     */
    boolean hasText();

}
