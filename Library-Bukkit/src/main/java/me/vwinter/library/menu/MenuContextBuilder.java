package me.vwinter.library.menu;

import com.google.common.collect.Lists;
import me.vwinter.library.menu.internal.InternalMenuContext;

import java.util.Arrays;
import java.util.List;

public class MenuContextBuilder {

    private String name;
    private final List<String> aliases;
    private int rows;
    private String title;

    public MenuContextBuilder() {
        this("undefined");
    }

    public MenuContextBuilder(final String name) {
        this(name, Lists.newArrayList());
    }

    public MenuContextBuilder(final String name, final List<String> aliases) {
        this(name, aliases, 3);
    }

    public MenuContextBuilder(final String name, final List<String> aliases, final int rows) {
        this(name, aliases, rows, "undefined");
    }

    public MenuContextBuilder(final String name, final List<String> aliases, final int rows, final String title) {
        this.name = name;
        this.aliases = aliases;
        this.rows = rows;
        this.title = title;
    }

    public MenuContextBuilder named(final String name) {
        this.name = name;
        return this;
    }

    public MenuContextBuilder withAliases(final String... aliases) {
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public MenuContextBuilder titled(final String title) {
        this.title = title;
        return this;
    }

    public MenuContextBuilder withRows(final int rows) {
        this.rows = rows;
        return this;
    }

    public MenuContext build() {
        return new InternalMenuContext(this.name, this.aliases.toArray(new String[]{}), this.rows, this.title);
    }
}
