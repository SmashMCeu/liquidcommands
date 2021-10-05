package de.liquiddev.command;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.base.Preconditions;

import de.liquiddev.util.common.EnumUtil;

public class DefaultCommandFailHandler<T> implements CommandFailHandler<T> {

	private static CommandFailHandler<?> instance = new DefaultCommandFailHandler<>();

	public static <T> CommandFailHandler<T> getDefault() {
		return (CommandFailHandler<T>) instance;
	}

	public static void setDefault(CommandFailHandler<?> defaultHandler) {
		Preconditions.checkNotNull(defaultHandler);
		instance = defaultHandler;
	}

	@Override
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided) {
		if (required.equals(Integer.class) || required.equals(Long.class)) {
			sender.sendMessage(command.getPrefix() + "§cInvalid argument! §e" + provided + " §cis not a number!");
		} else if (required.isEnum()) {
			Enum<?>[] values = EnumUtil.getValues((Class<Enum<?>>) required);
			String available = Stream.of(values).map(e -> e.name()).collect(Collectors.joining(", "));
			sender.sendMessage(command.getPrefix() + "§cInvalid argument: §e" + provided + "§c, available: §e" + available);
		} else if(required.getClass().getSimpleName().contains("Player")) {
			sender.sendMessage(command.getPrefix() + "§cPlayer §e" + provided + " §cnot found!");
		} else {
			sender.sendMessage(command.getPrefix() + "§cInvalid argument! §e" + provided + " §cis not a " + required.getSimpleName() + "!");
		}
	}

	@Override
	public void onMissingArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, int index) {
		sender.sendMessage(command.getPrefix() + "§cMissing arguments! Use §e" + command.getUsage());
	}

	@Override
	public void onPermissionFail(AbstractCommandSender<T> sender, CommandNode<T> command) {
		sender.sendMessage(command.getPrefix() + "§cYou do not have permission to execute this command.");
	}

	@Override
	public void onUnsupportedCommandSender(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> requiredSenderType) {
		sender.sendMessage(command.getPrefix() + "§cThis command can only be executed as a " + requiredSenderType.getSimpleName() + "!");
	}

	@Override
	public void onRateLimitExceeded(AbstractCommandSender<T> sender, CommandNode<T> command) {
		sender.sendMessage(command.getPrefix() + "§cPlease wait before doing this again.");
	}

	@Override
	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason) {
		sender.sendMessage(command.getPrefix() + "§c" + reason);
	}
}
