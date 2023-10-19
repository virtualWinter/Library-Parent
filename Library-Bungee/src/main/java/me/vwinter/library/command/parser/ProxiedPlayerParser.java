package me.vwinter.library.command.parser;

import com.google.common.collect.Lists;
import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.parametric.argument.ArgumentParser;
import me.vwinter.library.command.parametric.defaults.StringUtil;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProxiedPlayerParser implements ArgumentParser<ProxiedPlayer> {

    @Override
    public <C> ProxiedPlayer parse(WrappedSender sender, CommandContext<C> context, String argument) {
        ProxiedPlayer player = sender.get();
        UUID uuid;

        try {
            uuid = UUID.fromString(argument);
        } catch (IllegalArgumentException ignored) {
            return player.getServer().getInfo().getPlayers().stream().filter(other -> other.getName().equalsIgnoreCase(argument)).findFirst().orElse(null);
        }

        return player.getServer().getInfo().getPlayers().stream().filter(other -> other.getUniqueId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public String getParseMessage(WrappedSender sender, String invalid) {
        return String.format("&cCouldn't find a player named %s.", invalid);
    }

    @Override
    public <C> List<String> getSuggestions(WrappedSender sender, CommandContext<C> context, String lastWord) {
        ProxiedPlayer player = sender.get();

        if (lastWord.isEmpty()) {
            return player.getServer().getInfo().getPlayers().stream().map(ProxiedPlayer::getName).collect(Collectors.toList());
        }

        List<String> list = Lists.newArrayList();

        for (ProxiedPlayer other : player.getServer().getInfo().getPlayers()) {
            String name = other.getName();

            if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                list.add(name);
            }
        }

        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}
