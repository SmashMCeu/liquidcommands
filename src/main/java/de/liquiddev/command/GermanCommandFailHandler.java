package de.liquiddev.command;

public class GermanCommandFailHandler<T> extends DefaultCommandFailHandler<T> {

	public GermanCommandFailHandler(String prefix) {
		super(prefix);
	}

	@Override
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided) {
		if (required.equals(Integer.class) || required.equals(Long.class)) {
			sender.sendMessage(prefix + "§cUngültiger Befehl! §e" + provided + " §cist keine Zahl!");
		} else {
			sender.sendMessage(prefix + "§cUngültiger Befehl! §e" + provided + " §cist kein " + required.getSimpleName() + "!");
		}
	}

	@Override
	public void onMissingArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, int index) {
		sender.sendMessage(prefix + "§cUngültiger Befehl! Nutze §e" + command.getUsage());
	}

	@Override
	public void onPermissionFail(AbstractCommandSender<T> sender, CommandNode<T> command) {
		sender.sendMessage(prefix + "§cDu hast nicht genügend Rechte für diesen Befehl.");
	}

	@Override
	public void onUnsupportedCommandSender(AbstractCommandSender<?> sender, Class<?> requiredSenderType) {
		sender.sendMessage(prefix + "§cDieser Befehl kann nur als " + requiredSenderType.getSimpleName() + " ausgeführt werden!");
	}

	@Override
	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason) {
		sender.sendMessage(prefix + "§cBefehl konnte nicht ausgeführt werden: " + reason);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
