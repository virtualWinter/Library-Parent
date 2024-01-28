package me.vwinter.library.command.provider;

import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.parametric.provider.CommandSenderProvider;
import org.bukkit.entity.Player;

public class PlayerProvider implements CommandSenderProvider<Player> {

    @Override
    public Player get(WrappedSender sender) {
        if (!(sender.get() instanceof Player)) {
            return null;
        }

        return sender.get();
    }

    @Override
    public String getInvalidSenderMessage(WrappedSender sender) {
        return "&cSorry but this command is only executable by players.";
    }
}
