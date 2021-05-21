package de.liquiddev.command;

public class DefaultCommandFailHandler<T> implements CommandFailHandler<T> {

	private static CommandFailHandler<?> instance = new DefaultCommandFailHandler<>("");

	public static <T> CommandFailHandler<T> getDefault() {
		return (CommandFailHandler<T>) instance;
	}

	public static void setDefault(CommandFailHandler<?> defaultHandler) {
		instance = defaultHandler;
	}

	protected String prefix;

	public DefaultCommandFailHandler(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided) {
		if (required.equals(Integer.class) || required.equals(Long.class)) {
			sender.sendMessage(prefix + "§cInvalid command! §e" + provided + " §cis not a number!");
		} else {
			sender.sendMessage(prefix + "§cInvalid command! §e" + provided + " §cis not a " + required.getSimpleName() + "!");
		}
	}

	@Override
	public void onMissingArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, int index) {
		sender.sendMessage(prefix + "§cInvalid command! Use §e" + command.getUsage());
	}

	@Override
	public void onPermissionFail(AbstractCommandSender<T> sender, CommandNode<T> command) {
		sender.sendMessage(prefix + "§cYou do not have permission to execute this command.");
	}

	@Override
	public void onUnsupportedCommandSender(AbstractCommandSender<?> sender, Class<?> requiredSenderType) {
		sender.sendMessage(prefix + "§cThis command can only be executed as a " + requiredSenderType.getSimpleName() + "!");
	}

	@Override
	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason) {
		sender.sendMessage(prefix + "§c" + reason);
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
}
