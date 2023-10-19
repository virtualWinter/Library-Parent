package me.vwinter.library.command.provider;

import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.parametric.provider.CommandSenderProvider;
import net.md_5.bungee.api.CommandSender;

public class SenderProvider implements CommandSenderProvider<CommandSender> {

    @Override
    public CommandSender get(WrappedSender sender) {
        return sender.get();
    }

    @Override
    public String getInvalidSenderMessage(WrappedSender sender) {
        return null;
    }
}
