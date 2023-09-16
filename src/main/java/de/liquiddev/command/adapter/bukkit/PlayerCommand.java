package de.liquiddev.command.adapter.bukkit;

import org.bukkit.entity.Player;

public abstract class PlayerCommand extends BukkitCommand<Player> {
	public PlayerCommand(String name, String hint) {
		super(Player.class, name, hint);
	}

	public PlayerCommand(String name) {
		super(Player.class, name);
	}
}
