package de.liquiddev.command.adapter.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandNode;
import de.liquiddev.command.InvalidCommandArgException;
import de.liquiddev.command.MissingCommandArgException;

public class Arguments extends CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, AbstractCommandSender<?> sender, String[] args) {
		return new Arguments(command, sender, args);
	}

	Arguments(CommandNode<?> command, AbstractCommandSender<?> sender, String[] arguments) {
		super(command, sender, arguments);
	}

	public Player getPlayer(int index) throws CommandFailException {
		if (!isPresent(index)) {
			throw new MissingCommandArgException(getCommand(), Player.class, index);
		}
		String arg = get(index);
		Player player = Bukkit.getPlayer(arg);
		if (player == null) {
			throw new InvalidCommandArgException(getCommand(), Player.class, arg);
		}
		return player;
	}
}