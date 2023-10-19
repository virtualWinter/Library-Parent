package me.vwinter.library.menu.internal;

import me.vwinter.library.menu.MenuContext;

public class InternalMenuContext implements MenuContext {

    private final String name;
    private final String[] aliases;
    private final int rows;
    private final String title;

    public InternalMenuContext(final String name, final String[] aliases, int rows, final String title) {
        this.name = name;
        this.aliases = aliases;
        this.rows = rows;
        this.title = title;
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
    public int getRows() {
        return this.rows;
    }

    @Override
    public String getTitle() {
        return this.title;
    }
}
