package de.liquiddev.command.adapter.bungee;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandFailException;
import net.md_5.bungee.api.CommandSender;

public abstract class ProxySubCommand<T extends CommandSender> extends CommandChild<T> {

	public ProxySubCommand(Class<T> senderType, String name) {
		this(senderType, name, "");
	}

	public ProxySubCommand(Class<T> senderType, String name, String hint) {
		super(senderType, name, hint);
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		ProxyArguments arguments = (ProxyArguments) args;
		this.execute(sender.getSender(), arguments);
	}

	public abstract void execute(T sender, ProxyArguments args) throws CommandFailException;
}