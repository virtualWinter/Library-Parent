package me.vwinter.library.sidebar.internal;

import me.vwinter.library.sidebar.Sidebar;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.Collections;
import java.util.List;

class InternalSidebar implements Sidebar {

    private final Player player;
    private boolean showing;

    InternalSidebar(final Player player) {
        this.player = player;

        /* Check if player has the main server scoreboard
         * and if so, set it to a new scoreboard */
        Scoreboard scoreboard = getScoreboard();
        final ScoreboardManager manager = player.getServer().getScoreboardManager();
        if (scoreboard == manager.getMainScoreboard()) scoreboard = manager.getNewScoreboard();
        player.setScoreboard(scoreboard);

        /* Register the objective if absent and add 16
         * teams with unique entries */
        if (scoreboard.getObjective("vertic") == null)
            scoreboard.registerNewObjective("vertic", "dummy").setDisplayName("vertic");

        for (int i = 0; i < 16; i++) {
            getTeam(i).addEntry(ChatColor.values()[i].toString());
        }

        show();
    }

    @Override
    public void updateTitle(String title) {
        /* Substring title if over 32 characters and
         * update the objectives display-name */
        title = title.length() > 32 ? title.substring(0, 32) : title;
        Objective objective = getScoreboard().getObjective("vertic");

        objective.setDisplayName(color(title));
    }

    @Override
    public void updateLines(final List<String> list) {
        if (!showing) {
            return;
        }

        /* Reverse the list and loop through
         * scoreboard-lines and update teams */
        Collections.reverse(list);

        for (int i = 0; i < 16; i++) {
            if (list.size() < i + 1) {
                removeLine(i + 1);
            } else {
                updateLine(list.get(i), i + 1);
            }
        }
    }

    @Override
    public void show() {
        getScoreboard().getObjective("vertic").setDisplaySlot(DisplaySlot.SIDEBAR);
        this.showing = true;
    }

    @Override
    public void hide() {
        this.showing = false;
        getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
    }

    @Override
    public boolean isShowing() {
        return this.showing;
    }

    private Team getTeam(final int slot) {
        Team team = getScoreboard().getTeam("\u0000" + slot);
        if (team == null) team = getScoreboard().registerNewTeam("\u0000" + slot);
        return team;
    }

    private Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    private void removeLine(final int line) {
        getScoreboard().resetScores(ChatColor.values()[line].toString());
    }

    private void updateLine(final String string, final int line) {
        final Scoreboard scoreboard = getScoreboard();
        Objective objective = scoreboard.getObjective("vertic");

        String[] prefixSuffix = getPrefixSuffix(string);

        Team team = getTeam(line);
        team.setPrefix(prefixSuffix[0]);
        team.setSuffix(prefixSuffix[1]);

        objective.getScore(ChatColor.values()[line].toString()).setScore(line);
    }

    private String[] getPrefixSuffix(String s) {
        s = color(s);

        final String prefix = getPrefix(s);
        final String suffix = getPrefix(ChatColor.getLastColors(prefix) + getSuffix(s));
        return new String[]{prefix, suffix};
    }

    private String getPrefix(final String s) {
        return s.length() > 16 ? s.substring(0, 16) : s;
    }

    private String getSuffix(String s) {
        if (s.length() > 32) {
            s = s.substring(0, 32);
        }
        return s.length() > 16 ? s.substring(16) : "";
    }

    private String color(final String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }


}
