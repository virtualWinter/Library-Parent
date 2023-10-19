package me.vwinter.library.command;

import com.google.inject.Injector;
import me.vwinter.library.command.node.CommandNode;

public abstract class AbstractCommandRegistrar {

    public abstract <T> void registerCommand(final CommandNode<T> node, final Injector injector);

}
