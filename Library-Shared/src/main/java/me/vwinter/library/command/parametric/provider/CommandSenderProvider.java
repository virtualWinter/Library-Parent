package me.vwinter.library.command.parametric.provider;

import me.vwinter.library.command.WrappedSender;

public interface CommandSenderProvider<T> {

    T get(WrappedSender sender);

    String getInvalidSenderMessage(WrappedSender sender);

}
