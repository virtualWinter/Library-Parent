# Library-Parent

## Examples
### 1. Module

```java
public class ExampleModule extends AbstractModule {

    @PostInject
    public void registerCommands(final CommandService service, final Injector injector) {
        service.registerNodes(ExampleCommands.class, injector);
    }

    @PostInject
    public void registerMenus(final MenuHolderFactory factory) {
        factory.createMenu(ExampleMenu.class);
    }

}
```

### 2. Commands

```java
@Command(aliases = "poke", description = "Poke someone.")
public void poke(@Sender Player player, @Argument("player") Player other) {
    other.playSound(other.getLocation(), Sound.GLASS, 100, 50);
    player.sendMessage(color("&eYou have poked &a" + other.getName() + "&e."));
    other.sendMessage(color("&eYou have been poked by &a" + player.getName() + "&e!"));
}
```

### 3. Menu

```java
@MenuMeta(aliases = "example", title = "&lTesting")
public class ExampleMenu implements Menu {

    @Override
    public Table<Integer, ItemStack, MenuAction> render(Player player) {
        Table<Integer, ItemStack, MenuAction> table = HashBasedTable.create();

        /* Fill bedrock around the inventory. */
        Menus.fill(table, new ItemStack(Material.BEDROCK), 3, RowType.AROUND);

        /* Set a nether star in the middle. */
        table.put(13, new ItemStack(Material.NETHER_STAR), clickType -> {
            player.closeInventory();
            player.sendMessage(ChatColor.GOLD + "You just clicked a star!");
            return true;
        });

        return table;
    }

}
```

### 4. Sidebar

```java
public class TestSidebarProvider implements SidebarProvider {

    private final Server server;

    @Inject
    public DefaultSidebarProvider(final Server server) {
        this.server = server;
    }

    @Override
    public Function<Player, String> getTitle() {
        return player -> "&dCatMC";
    }

    @Override
    public Function<Player, List<String>> getLines() {
        return player -> Arrays.asList(
                "&7&m----------------------",
                "&bOnline: &f" + server.getOnlinePlayers().size(),
                " ",
                "&bYou: &f" + player.getName(),
                "&7&m----------------------"
        );
    }

    @Override
    public Function<Player, Boolean> isShowing() {
        return player -> true;
    }
}
```

### 5. Nametag

```java
public class TestNameTagProvider implements NameTagProvider {

    @Override
    public Function<Player, String> getPrefix() {
        return player -> "&7[Prefix] &e";
    }

    @Override
    public Function<Player, String> getSuffix() {
        return player -> " &7(Suffix)";
    }
}
```
