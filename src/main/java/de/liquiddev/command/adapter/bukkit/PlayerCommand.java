package de.liquiddev.command.adapter.bukkit;

import org.bukkit.entity.Player;

public abstract class PlayerCommand extends BukkitCommand<Player> {

	@Deprecated
	public PlayerCommand(String prefix, String name, String hint) {
		super(Player.class, prefix, name, hint);
	}

	public PlayerCommand(String name, String hint) {
		super(Player.class, name, hint);
	}
}
