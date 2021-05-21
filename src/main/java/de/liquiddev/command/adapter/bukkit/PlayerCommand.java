package de.liquiddev.command.adapter.bukkit;

import org.bukkit.entity.Player;

public abstract class PlayerCommand extends BukkitCommand<Player> {

	public PlayerCommand(String prefix, String name, String hint) {
		super(Player.class, prefix, name, hint);
	}
}