package de.liquiddev.command;

public interface CommandFailHandler<T> {
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided);

	public void onMissingArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, int index);

	public void onPermissionFail(AbstractCommandSender<T> sender, CommandNode<T> command);

	public void onUnsupportedCommandSender(AbstractCommandSender<?> sender, Class<?> requiredSenderType);

	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason);
}