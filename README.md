# LiquidCommands library


## Maven
```
<repository>
	<id>smashmc-public</id>
	<url>https://repo.smashmc.eu/public</url>
</repository>
```

```
<dependency>
	<groupId>de.liquiddev</groupId>
	<artifactId>commands</artifactId>
	<version>1.3.0</version>
</dependency>
```

## Features
* Dynamic command nodes
* Automatic autocomplete with custom autocompletors
* Variable arguments
* Rate limit / cooldown system
* Bukkit, Bungeecord & Velocity support
* QuickCommands as one-liner
* Command context

## Code snippets

### Register a command
To register a command, just use ```command.register(plugin);```
There is no need to add them to the plugin.yml.

### Set global default prefix
To change the default chat prefix used for all commands, use `Commands.setDefaultPrefix(...)`.

### Quick bukkit command
```
QuickCommand.name("examplecommand")
	.alias("example")
	.permission("example.command")
	.prefix("Example: ")
	.register(plugin, (player, args) -> {
		player.sendMessage("I'm a quick example command");
	});
```

### Bukkit command for players
```
public class ExampleBukkitCommand extends PlayerCommand {

	public ExampleBukkitCommand() {
		super("examplecommand", "<player>");
		this.addAlias("example");
		this.setPermission("example.command");
		this.setAutocompleter(0, Autocomplete.players());
		this.setDescription("A simple example command.");
	}

	@Override
	protected void onCommand(Player sender, Arguments args) throws CommandFailException {
		Player player = args.getPlayer(0);
		player.sendMessage(getPrefix() + "Hey, " + sender.getName() + " poked you!");
		sender.sendMessage(getPrefix() + "You poked " + player.getName());
	}
}
```

### Bungeecord command for players & console
```
public class ExampleBungeeCommand extends ProxyCommand<CommandSender> {

	public ExampleBungeeCommand() {
		super(CommandSender.class, "examplecommand", "<player>");
		this.setPermission("example.command");
		this.setAutocompleter(0, Autocomplete.proxyPlayers());
		this.setDescription("A simple example command.");
	}

	@Override
	protected void onCommand(CommandSender sender, ProxyArguments args) throws CommandFailException {
		ProxiedPlayer target = args.getPlayer(0);
		target.sendMessage(getPrefix() + "Hey, " + sender.getName() + " poked you!");
		sender.sendMessage(getPrefix() + "You poked " + target.getName());
	}
}
```


