package me.vwinter.library.nametag;

import com.google.inject.Inject;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

public class NameTagRunnable implements Runnable {

    private final Server server;
    private final NameTagService service;

    @Inject
    public NameTagRunnable(final Server server, final NameTagService service) {
        this.server = server;
        this.service = service;
    }

    @Override
    public void run() {
        for (final Player player : server.getOnlinePlayers()) {
            for (final Player other : server.getOnlinePlayers()) {
                Team team = player.getScoreboard().getTeam(other.getName());
                final NameTag nameTag = service.getNameTag(other);
                final NameTagProvider provider = service.getProvider();

                /* If the provider and nameTag are present, set
                 * the prefix/suffix from the provider */
                if (provider != null) {
                    if (nameTag != null) {
                        nameTag.setPrefix(provider.getPrefix().apply(other));
                        nameTag.setSuffix(provider.getSuffix().apply(other));
                    }
                }

                /* If team is absent, register a new and add the
                 * other player's name as entry */
                if (team == null) {
                    team = player.getScoreboard().registerNewTeam(other.getName());
                    team.addEntry(other.getName());
                }

                /* If the nameTag is absent, clear prefix/suffix */
                if (nameTag == null) {
                    team.setPrefix("");
                    team.setSuffix("");
                    continue;
                }

                /* Finally set the prefix/suffix for the team with translated chatColors */
                team.setPrefix(ChatColor.translateAlternateColorCodes('&', nameTag.getPrefix()));
                team.setSuffix(ChatColor.translateAlternateColorCodes('&', nameTag.getSuffix()));
            }
        }
    }
}
