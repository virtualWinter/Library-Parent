package me.vwinter.library.command.provider;

import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.parametric.provider.CommandSenderProvider;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlayerProvider implements CommandSenderProvider<ProxiedPlayer> {

    @Override
    public ProxiedPlayer get(WrappedSender sender) {
        if (!(sender.get() instanceof ProxiedPlayer)) {
            return null;
        }

        return sender.get();
    }

    @Override
    public String getInvalidSenderMessage(WrappedSender sender) {
        return "&cSorry but this command is only executable by players.";
    }
}
