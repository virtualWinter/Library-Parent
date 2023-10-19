package me.vwinter.library.example;

import com.google.inject.Inject;
import me.vwinter.library.command.annotation.*;
import me.vwinter.library.menu.MenuHolder;
import me.vwinter.library.menu.MenuHolderService;
import me.vwinter.library.storage.named.NamedStore;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class ExampleCommands {

    private final MenuHolderService service;
    private final NamedStore<MenuHolder> store;

    @Inject
    public ExampleCommands(MenuHolderService service, NamedStore<MenuHolder> store) {
        this.service = service;
        this.store = store;
    }

    @Command(aliases = "menu")
    public void menu(@Sender Player player) {
        service.openHolder(player, store.getByName("example"));
    }

    @Command(aliases = "poke", description = "Poke someone.")
    public void poke(@Sender Player player, @Argument("player") Player other) {
        other.playSound(other.getLocation(), Sound.GLASS, 100, 50);
        player.sendMessage(color("&eYou have poked &a" + other.getName() + "&e."));
        other.sendMessage(color("&eYou have been poked by &a" + player.getName() + "&e!"));
    }

    @Command(aliases = "bukkit first", description = "Get the amount of online players.")
    public void exampleFirst(@Sender Player player, @Injected Server server) {
        int online = server.getOnlinePlayers().size();
        player.sendMessage(color("&eThere are currently " + online + " players online."));
    }

    @Command(aliases = "bukkit second", description = "Send a message to yourself.")
    public void exampleSecond(@Sender Player player, @Argument("message") @Text String text) {
        player.sendMessage(color(text));
    }

    @Command(aliases = "bukkit test third", description = "Test <3")
    public void exampleThird(@Sender Player player) {
        player.sendMessage(color("&4Test <3"));
    }

    private String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
