package de.liquiddev.command;

public class DefaultCommandFailHandler<T> implements CommandFailHandler<T> {

	private static CommandFailHandler<?> instance = new DefaultCommandFailHandler<>();

	public static <T> CommandFailHandler<T> getDefault() {
		return (CommandFailHandler<T>) instance;
	}

	public static void setDefault(CommandFailHandler<?> defaultHandler) {
		instance = defaultHandler;
	}

	@Override
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided) {
		if (required.equals(Integer.class) || required.equals(Long.class)) {
			sender.sendMessage(command.getPrefix() + "§cInvalid command! §e" + provided + " §cis not a number!");
		} else {
			sender.sendMessage(command.getPrefix() + "§cInvalid command! §e" + provided + " §cis not a " + required.getSimpleName() + "!");
		}
	}

	@Override
	public void onMissingArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, int index) {
		sender.sendMessage(command.getPrefix() + "§cInvalid command! Use §e" + command.getUsage());
	}

	@Override
	public void onPermissionFail(AbstractCommandSender<T> sender, CommandNode<T> command) {
		sender.sendMessage(command.getPrefix() + "§cYou do not have permission to execute this command.");
	}

	@Override
	public void onUnsupportedCommandSender(AbstractCommandSender<?> sender, CommandNode<T> command, Class<?> requiredSenderType) {
		sender.sendMessage(command.getPrefix() + "§cThis command can only be executed as a " + requiredSenderType.getSimpleName() + "!");
	}

	@Override
	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason) {
		sender.sendMessage(command.getPrefix() + "§c" + reason);
	}
}
