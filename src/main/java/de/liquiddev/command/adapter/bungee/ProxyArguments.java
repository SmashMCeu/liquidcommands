package de.liquiddev.command.adapter.bungee;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandNode;
import de.liquiddev.command.InvalidCommandArgException;
import de.liquiddev.command.MissingCommandArgException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ProxyArguments extends CommandArguments {

	public static CommandArguments fromStrings(CommandNode<?> command, AbstractCommandSender<?> sender, String[] args) {
		return new ProxyArguments(command, sender, args);
	}

	ProxyArguments(CommandNode<?> command, AbstractCommandSender<?> sender, String[] arguments) {
		super(command, sender, arguments);
	}

	public ProxiedPlayer getPlayer(int index) throws CommandFailException {
		if (!isPresent(index)) {
			throw new MissingCommandArgException(getCommand(), ProxiedPlayer.class, index);
		}
		String arg = get(index);
		ProxiedPlayer player = ProxyServer.getInstance()
				.getPlayer(arg);
		if (player == null) {
			throw new InvalidCommandArgException(getCommand(), ProxiedPlayer.class, arg);
		}
		return player;
	}
}