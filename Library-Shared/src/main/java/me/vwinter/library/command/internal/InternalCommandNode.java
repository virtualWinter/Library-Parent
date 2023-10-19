package me.vwinter.library.command.internal;

import com.google.common.collect.Lists;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.node.CommandNode;

import java.lang.reflect.Method;
import java.util.List;

class InternalCommandNode<T> implements CommandNode<T> {

    private final boolean root;
    private final List<CommandNode<T>> childNodes;
    private final CommandNode<T> parentNode;
    private CommandContext<T> context;
    private final T type;
    private final Class<T> typeClass;
    private Method method;

    InternalCommandNode(final boolean root, final List<CommandNode<T>> childNodes, final CommandNode<T> parentNode, final CommandContext<T> context, final T type, final Class<T> typeClass, final Method method) {
        this.root = root;
        this.childNodes = childNodes;
        this.parentNode = parentNode;
        this.context = context;
        this.type = type;
        this.typeClass = typeClass;
        this.method = method;
    }

    @Override
    public boolean isRootNode() {
        return this.root;
    }

    @Override
    public List<CommandNode<T>> getChildNodes() {
        return this.childNodes;
    }

    @Override
    public CommandNode<T> getParentNode() {
        return this.parentNode;
    }

    @Override
    public CommandNode<T> registerChildNode(final CommandContext<T> context, final Method method) {
        final CommandNode<T> childNode =
                new InternalCommandNode<>(
                        false, Lists.newArrayList(), this, context, this.type, this.typeClass, method);

        this.childNodes.add(childNode);
        return childNode;
    }

    @Override
    public CommandContext<T> getContext() {
        return this.context;
    }

    @Override
    public void updateContext(CommandContext<T> context) {
        this.context = context;
    }

    @Override
    public T getInstance() {
        return this.type;
    }

    @Override
    public Method getMethod() {
        return this.method;
    }

    @Override
    public void updateMethod(Method method) {
        this.method = method;
    }
}
