package de.liquiddev.command.autocomplete;

import java.util.function.BiFunction;

import org.bukkit.command.CommandSender;

public class BukkitPermissionChecker implements BiFunction<CommandSender, String, Boolean> {
	@Override
	public Boolean apply(CommandSender t, String u) {
		return t.hasPermission(u);
	}
}
