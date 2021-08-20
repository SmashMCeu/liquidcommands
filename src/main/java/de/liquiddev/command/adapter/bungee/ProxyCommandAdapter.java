package de.liquiddev.command.adapter.bungee;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.adapter.AbstractCommandAdapter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.TabExecutor;

class ProxyCommandAdapter extends AbstractCommandAdapter<CommandSender> {

	private CommandListener commandListener;

	public ProxyCommandAdapter(ProxyCommand<?> proxyCommand, String name, String[] aliases) {
		super(proxyCommand);
		this.commandListener = new CommandListener(name, aliases);
		proxyCommand.setAliases(aliases);
	}

	public void register(Plugin plugin) {
		ProxyServer.getInstance()
				.getPluginManager()
				.registerCommand(plugin, commandListener);
	}

	@Override
	public AbstractCommandSender<CommandSender> abstractSender(CommandSender sender) {
		return new ProxyCommandSender<CommandSender>(sender);
	}

	@Override
	public CommandArguments getArguments(String[] args) {
		return ProxyArguments.fromStrings(getCommand(), args);
	}

	private class CommandListener extends Command implements TabExecutor {
		public CommandListener(String name, String[] aliases) {
			super(name, null /* <-- we override getter */, aliases);
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			ProxyCommandAdapter.this.onCommand(sender, args);
		}

		@Override
		public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
			return ProxyCommandAdapter.this.onTabComplete(sender, args);
		}

		@Override
		public String getPermission() {
			return getCommand().getPermission();
		}

		@Override
		public boolean hasPermission(CommandSender sender) {
			AbstractCommandSender<CommandSender> abstractSender = new ProxyCommandSender<CommandSender>(sender);
			return getCommand().hasPermission(abstractSender);
		}

		@Override
		public String[] getAliases() {
			return ProxyCommandAdapter.this.getCommand()
					.getAliases();
		}
	}
}