package de.liquiddev.command;

public class GermanCommandFailHandler<T> extends DefaultCommandFailHandler<T> {

	@Override
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided) {
		if (required.equals(Integer.class) || required.equals(Long.class)) {
			sender.sendMessage(command.getPrefix() + "§cUngültiger Befehl! §e" + provided + " §cist keine Zahl!");
		} else {
			sender.sendMessage(command.getPrefix() + "§cUngültiger Befehl! §e" + provided + " §cist kein " + required.getSimpleName() + "!");
		}
	}

	@Override
	public void onMissingArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, int index) {
		sender.sendMessage(command.getPrefix() + "§cUngültiger Befehl! Nutze §e" + command.getUsage());
	}

	@Override
	public void onPermissionFail(AbstractCommandSender<T> sender, CommandNode<T> command) {
		sender.sendMessage(command.getPrefix() + "§cDu hast nicht genügend Rechte für diesen Befehl.");
	}

	@Override
	public void onUnsupportedCommandSender(AbstractCommandSender<?> sender, CommandNode<T> command, Class<?> requiredSenderType) {
		sender.sendMessage(command.getPrefix() + "§cDieser Befehl kann nur als " + requiredSenderType.getSimpleName() + " ausgeführt werden!");
	}

	@Override
	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason) {
		sender.sendMessage(command.getPrefix() + "§cBefehl konnte nicht ausgeführt werden: " + reason);
	}
}
