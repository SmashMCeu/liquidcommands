package de.liquiddev.command.adapter.bungee;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandRoot;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

public abstract class ProxyCommand<T extends CommandSender> extends CommandRoot<T> {

	private ProxyCommandAdapter adapter;

	public ProxyCommand(Class<T> senderType, String name, String hint, String... aliases) {
		super(senderType, name, hint);
		this.adapter = new ProxyCommandAdapter(this, name, aliases);
	}

	@Deprecated
	public ProxyCommand(String prefix, Class<T> senderType, String name, String hint, String... aliases) {
		super(senderType, name, hint, prefix);
		this.adapter = new ProxyCommandAdapter(this, name, aliases);
	}

	public void register(Plugin plugin) {
		adapter.register(plugin);
	}

	@Override
	protected void onCommand(AbstractCommandSender<T> sender, CommandArguments args) throws CommandFailException {
		this.onCommand(sender.getSender(), (ProxyArguments) args);
	}

	protected abstract void onCommand(T sender, ProxyArguments args) throws CommandFailException;
}