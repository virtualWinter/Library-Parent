package me.vwinter.library.command;

import com.google.inject.Key;
import me.vwinter.library.command.parser.BukkitStringParser;
import me.vwinter.library.command.parser.PlayerParser;
import me.vwinter.library.command.provider.ConsoleProvider;
import me.vwinter.library.command.provider.PlayerProvider;
import me.vwinter.library.command.provider.SenderProvider;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class BukkitCommandModule extends AbstractCommandModule {

    private final CommandMap commandMap;

    public BukkitCommandModule(final CommandMap commandMap) {
        this.commandMap = commandMap;
    }

    @Override
    public AbstractCommandRegistrar getRegistrar() {
        return new BukkitCommandRegistrar(this.commandMap);
    }

    @Override
    public void registerDefaults(CommandService service) {
        service.registerParser(Key.get(String.class), new BukkitStringParser());
        service.registerParser(Key.get(Player.class), new PlayerParser());

        service.registerProvider(Key.get(CommandSender.class), new SenderProvider());
        service.registerProvider(Key.get(Player.class), new PlayerProvider());
        service.registerProvider(Key.get(ConsoleCommandSender.class), new ConsoleProvider());

    }
}
