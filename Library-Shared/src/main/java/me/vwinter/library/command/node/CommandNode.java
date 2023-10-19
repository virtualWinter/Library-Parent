package me.vwinter.library.command.node;

import java.lang.reflect.Method;
import java.util.List;

public interface CommandNode<T> {

    /**
     * Returns a boolean whether this is the root node or not.
     */
    boolean isRootNode();

    /**
     * Returns a list of child nodes.
     */
    List<CommandNode<T>> getChildNodes();

    /**
     * Returns this node's parent if present.
     */
    CommandNode<T> getParentNode();

    /**
     * Register a children for this node.
     *
     * <p>The children will be added to this node's list of
     * child-nodes. </p>
     *
     * @param context the context.
     * @param method  the method.
     * @return Returns the new children node registered.
     */
    CommandNode<T> registerChildNode(final CommandContext<T> context, final Method method);

    /**
     * Returns the context for this command.
     */
    CommandContext<T> getContext();

    /**
     * Update the context for this command.
     */
    void updateContext(CommandContext<T> context);

    /**
     * Returns the instance of this command's class.
     */
    T getInstance();

    /**
     * Returns the method of this command.
     */
    Method getMethod();

    /**
     * Update the method for this command.
     */
    void updateMethod(Method method);

}
