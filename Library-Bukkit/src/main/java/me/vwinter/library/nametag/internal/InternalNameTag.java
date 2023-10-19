package me.vwinter.library.nametag.internal;

import me.vwinter.library.nametag.NameTag;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

class InternalNameTag implements NameTag {

    private String prefix, suffix;

    InternalNameTag(final Player player) {
        this.prefix = "";
        this.suffix = "";

        final Scoreboard scoreboard = player.getScoreboard();
        final ScoreboardManager manager = player.getServer().getScoreboardManager();

        if (scoreboard == manager.getMainScoreboard()) {
            player.setScoreboard(manager.getNewScoreboard());
        }
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(final String prefix) {
        this.prefix = sub(prefix);
    }

    @Override
    public String getSuffix() {
        return suffix;
    }

    @Override
    public void setSuffix(final String suffix) {
        this.suffix = sub(suffix);
    }

    private String sub(final String string) {
        return string.length() > 16 ? string.substring(0, 16) : string;
    }

}
