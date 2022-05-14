package de.liquiddev.command;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.liquiddev.command.identity.McIdentity;
import de.liquiddev.command.identity.McIdentityToken;
import de.liquiddev.util.common.EnumUtil;

public class GermanCommandFailHandler<T> extends DefaultCommandFailHandler<T> {

	@Override
	public void onInvalidArgument(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> required, String provided) {
		if (Number.class.isAssignableFrom(required)) {
			sender.sendMessage(command.getPrefix() + "§cUngültige Parameter! §e" + provided + " §cist keine Zahl!");
		} else if (required.equals(Duration.class)) {
			sender.sendMessage(command.getPrefix() + "§cUngültige Zeitangabe. Beispiel: §e7d30h10m");
		} else if (required.isEnum()) {
			Enum<?>[] values = EnumUtil.getValues((Class<Enum<?>>) required);
			String available = Stream.of(values)
					.map(e -> e.name())
					.collect(Collectors.joining(", "));
			sender.sendMessage(command.getPrefix() + "§cUngültiger Paramter: §e" + provided + "§c, verfügbar: §e" + available);
		} else if (required.equals(McIdentity.class) || required.equals(McIdentityToken.class)) {
			sender.sendMessage(command.getPrefix() + "§cSpieler §e" + provided + " §cwurde nicht gefunden!");
		} else if (required.getSimpleName()
				.contains("Player")) {
			sender.sendMessage(command.getPrefix() + "§cSpieler §e" + provided + " §cist nicht online!");
		} else {
			sender.sendMessage(command.getPrefix() + "§cUngültige Parameter! §e" + provided + " §cist kein " + required.getSimpleName() + "!");
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
	public void onUnsupportedCommandSender(AbstractCommandSender<T> sender, CommandNode<T> command, Class<?> requiredSenderType) {
		sender.sendMessage(command.getPrefix() + "§cDieser Befehl kann nur als " + requiredSenderType.getSimpleName() + " ausgeführt werden!");
	}

	@Override
	public void onCommandFail(AbstractCommandSender<T> sender, CommandNode<T> command, String reason) {
		sender.sendMessage(command.getPrefix() + "§cBefehl konnte nicht ausgeführt werden: " + reason);
	}
}
