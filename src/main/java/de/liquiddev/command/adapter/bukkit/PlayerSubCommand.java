package de.liquiddev.command.adapter.bukkit;

import org.bukkit.entity.Player;

public abstract class PlayerSubCommand extends SubCommand<Player> {
	@Deprecated
	public PlayerSubCommand(String name, String hint, String... aliases) {
		super(Player.class, name, hint);
		this.setAliases(aliases);
	}

	public PlayerSubCommand(String name, String hint) {
		super(Player.class, name, hint);
	}
}
