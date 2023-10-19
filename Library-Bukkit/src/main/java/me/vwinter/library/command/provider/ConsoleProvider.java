package me.vwinter.library.command.provider;

import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.parametric.provider.CommandSenderProvider;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleProvider implements CommandSenderProvider<ConsoleCommandSender> {

    @Override
    public ConsoleCommandSender get(WrappedSender sender) {
        if (!(sender.get() instanceof ConsoleCommandSender)) {
            return null;
        }

        return sender.get();
    }

    @Override
    public String getInvalidSenderMessage(WrappedSender sender) {
        return "&7Sorry but this command is only executable by console.";
    }
}
