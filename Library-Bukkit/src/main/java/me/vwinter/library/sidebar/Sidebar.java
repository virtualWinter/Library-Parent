package me.vwinter.library.sidebar;

import java.util.List;

public interface Sidebar {

    /**
     * Update the title
     *
     * @param title the title
     */
    void updateTitle(final String title);

    /**
     * Update a list of lines, will clear old entries
     *
     * @param list the list
     */
    void updateLines(final List<String> list);

    /**
     * Show the sidebar
     */
    void show();

    /**
     * Hide the sidebar
     */
    void hide();

    /**
     * Check whether this sidebar is showing or not
     *
     * @return boolean
     */
    boolean isShowing();

}
