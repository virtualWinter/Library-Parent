package me.vwinter.library.example;

import me.vwinter.library.command.annotation.*;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ExampleCommands {

    @Command(aliases = "globalpoke", description = "Poke someone globally.")
    public void poke(@Sender ProxiedPlayer player, @Argument("player") ProxiedPlayer other) {
        player.sendMessage(color("&eYou have poked &a" + other.getName() + "&e."));
        other.sendMessage(color("&eYou have been poked by &a" + player.getName() + "&e!"));
    }

    @Command(aliases = "bungee first", description = "Get the amount of global online players.")
    public void exampleFirst(@Sender ProxiedPlayer player, @Injected ProxyServer server) {
        int online = server.getOnlineCount();
        player.sendMessage(color("&eThere are currently " + online + " players online."));
    }

    @Command(aliases = "bungee second", description = "Send a message to yourself.")
    public void exampleSecond(@Sender ProxiedPlayer player, @Argument("message") @Text String text) {
        player.sendMessage(color(text));
    }

    private String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
