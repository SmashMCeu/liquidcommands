package de.liquiddev.command.adapter.bukkit;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandRoot;
import de.liquiddev.command.adapter.AbstractCommandAdapter;

class BukkitCommandAdapter extends AbstractCommandAdapter<CommandSender> {

	private CommandListener listener;

	public BukkitCommandAdapter(BukkitCommand<?> command) {
		super(command);
		this.listener = new CommandListener(command.getName(), command.getDescription(), command.getUsage(), Arrays.asList(command.getAliases()));
	}

	public void register(Plugin plugin) {
		CommandRoot<?> root = this.getCommand();
		String name = root.getName();

		try {
			Server server = Bukkit.getServer();
			Method commandMapGetter = server.getClass()
					.getMethod("getCommandMap");
			CommandMap commandMap = (CommandMap) commandMapGetter.invoke(server);

			var exists = Bukkit.getPluginCommand(name);
			if (exists != null) {
				if (plugin.equals(exists.getPlugin())) {
					throw new IllegalStateException("command '" + name + "' already registered through plugin.yml");
				} else {
					var otherPlugin = exists.getPlugin();
					throw new IllegalStateException("command '" + name + "' already registered by " + otherPlugin.getName());
				}
			}

			commandMap.register(plugin.getDescription()
					.getName(), listener);
		} catch (Exception ex) {
			throw new RuntimeException("could not register bukkit command", ex);
		}
	}

	@Override
	public AbstractCommandSender<CommandSender> abstractSender(CommandSender sender) {
		return new BukkitCommandSender<>(sender);
	}

	@Override
	public CommandArguments getArguments(String[] args, AbstractCommandSender<CommandSender> sender) {
		return Arguments.fromStrings(this.getCommand(), sender, args);
	}

	public class CommandListener extends Command {

		protected CommandListener(String name, String description, String usage, List<String> aliases) {
			super(name, description, usage, aliases);
		}

		@Override
		public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
			return BukkitCommandAdapter.super.onTabComplete(sender, args);
		}

		@Override
		public boolean execute(CommandSender sender, String cmd, String[] args) {
			BukkitCommandAdapter.super.onCommand(sender, args);
			return true;
		}

		@Override
		public String getName() {
			return getCommand().getName();
		}

		@Override
		public String getUsage() {
			return getCommand().getUsage();
		}

		@Override
		public String getDescription() {
			String description = getCommand().getDescription();
			return description != null ? description : "";
		}

		@Override
		public String getPermission() {
			return getCommand().getPermission();
		}

		@Override
		public List<String> getAliases() {
			return Arrays.asList(getCommand().getAliases());
		}
	}
}