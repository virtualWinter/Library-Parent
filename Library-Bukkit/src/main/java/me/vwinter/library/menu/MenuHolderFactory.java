package me.vwinter.library.menu;

public interface MenuHolderFactory {

    /**
     * Create a new {@link MenuHolder}.
     *
     * <p>The class is instantiated using an injector to inject dependencies.
     * A {@link MenuContext} will also be created for this holder. </p>
     *
     * @param menuClass the class.
     * @param <M>       the type.
     * @return Returns a new {@link MenuHolder}.
     */
    <M extends Menu> MenuHolder createMenu(final Class<? extends M> menuClass);

    /**
     * Create a new {@link MenuHolder}.
     *
     * @param menu    the menu.
     * @param <M>     the type.
     * @param context the context.
     * @return Returns a new {@link MenuHolder}.
     */
    <M extends Menu> MenuHolder createMenu(final M menu, final MenuContext context);

}
