package de.liquiddev.command.adapter.bukkit;

import org.bukkit.entity.Player;

public abstract class PlayerSubCommand extends SubCommand<Player> {
	public PlayerSubCommand(String name, String hint) {
		super(Player.class, name, hint);
	}

	public PlayerSubCommand(String name) {
		super(Player.class, name);
	}
}
