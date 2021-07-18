package de.liquiddev.command.adapter.bukkit.quick;

import org.bukkit.entity.Player;

import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.adapter.bukkit.Arguments;

public interface QuickCommandExecutor {

	public void execute(Player player, Arguments args) throws CommandFailException;

}
