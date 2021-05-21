package de.liquiddev.command.adapter.bukkit;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.adapter.AbstractCommandAdapter;

class BukkitCommandAdapter extends AbstractCommandAdapter<CommandSender> implements CommandExecutor, TabCompleter {

	public BukkitCommandAdapter(BukkitCommand<?> command) {
		super(command);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String var3, String[] args) {
		return this.onCommand(sender, args);
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String arg2, String[] args) {
		return this.onTabComplete(sender, args);
	}

	@Override
	public AbstractCommandSender<CommandSender> abstractSender(CommandSender sender) {
		return new BukkitCommandSender<>(sender);
	}

	@Override
	public CommandArguments getArguments(String[] args) {
		return Arguments.fromStrings(this.getCommand(), args);
	}
}