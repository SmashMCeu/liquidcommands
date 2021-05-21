package de.liquiddev.command.adapter.bukkit;

import org.bukkit.command.CommandSender;

public abstract class ConsoleSubCommand extends SubCommand<CommandSender> {
	@Deprecated
	public ConsoleSubCommand(String name, String hint, String... aliases) {
		super(CommandSender.class, name, hint);
		this.setAliases(aliases);
	}

	public ConsoleSubCommand(String name, String hint) {
		super(CommandSender.class, name, hint);
	}
}