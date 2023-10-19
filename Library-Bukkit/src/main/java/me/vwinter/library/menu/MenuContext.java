package me.vwinter.library.menu;

public interface MenuContext {

    /**
     * Returns the name for the menu.
     */
    String getName();

    /**
     * Returns the aliases for the menu.
     */
    String[] getAliases();

    /**
     * Returns the amount of rows for the menu.
     */
    int getRows();

    /**
     * Returns the title for the menu.
     */
    String getTitle();

}
