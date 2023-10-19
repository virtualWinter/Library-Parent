package me.vwinter.library.command.parser;

import me.vwinter.library.command.WrappedSender;
import me.vwinter.library.command.node.CommandContext;
import me.vwinter.library.command.parametric.argument.ArgumentParser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class PlayerParser implements ArgumentParser<Player> {

    @Override
    public <C> Player parse(WrappedSender sender, CommandContext<C> context, String argument) {
        CommandSender bukkitSender = sender.get();
        UUID uuid;

        try {
            uuid = UUID.fromString(argument);
        } catch (IllegalArgumentException ignored) {
            return bukkitSender.getServer().getPlayer(argument);
        }

        return bukkitSender.getServer().getPlayer(uuid);
    }

    @Override
    public String getParseMessage(WrappedSender sender, String invalid) {
        return String.format("&cCouldn't find a player named %s.", invalid);
    }

    @Override
    public <C> List<String> getSuggestions(WrappedSender sender, CommandContext<C> context, String lastWord) {
        Player player = sender.get();

        if (lastWord.isEmpty()) {
            return player.getServer().getOnlinePlayers().stream().filter(player::canSee).map(Player::getName).collect(Collectors.toList());
        }

        List<String> list = new ArrayList<>();

        for (Player other : player.getServer().getOnlinePlayers()) {
            String name = other.getName();
            if (!player.canSee(other)) {
                continue;
            }

            if (StringUtil.startsWithIgnoreCase(name, lastWord)) {
                list.add(name);
            }
        }

        list.sort(String.CASE_INSENSITIVE_ORDER);
        return list;
    }
}
