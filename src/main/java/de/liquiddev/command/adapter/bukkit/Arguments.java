package de.liquiddev.command.adapter.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandNode;
import de.liquiddev.command.InvalidCommandArgException;
import de.liquiddev.command.MissingCommandArgException;

public class Arguments extends CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, String[] args) {
		return new Arguments(command, args);
	}

	public Arguments(CommandNode<?> command, String[] arguments) {
		super(command, arguments);
	}

	public Player getPlayer(int index) throws CommandFailException {
		if (getArguments().length <= index) {
			throw new MissingCommandArgException(getCommand(), Player.class, index);
		}
		String arg = getArguments()[index];
		Player player = Bukkit.getPlayer(arg);
		if (arg == null) {
			throw new InvalidCommandArgException(getCommand(), Player.class, arg);
		}
		return player;
	}
}