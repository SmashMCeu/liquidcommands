package de.liquiddev.command.adapter;

import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandRoot;

public abstract class AbstractCommandAdapter<T> {

	private CommandRoot<?> command;

	public AbstractCommandAdapter(CommandRoot<?> command) {
		Preconditions.checkNotNull(command, "command must not be null");
		this.command = command;
	}

	public void onCommand(T sender, String[] args) {
		AbstractCommandSender<T> abstractSender = abstractSender(sender);
		try {
			command.executeCommand(abstractSender, getArguments(args));
		} catch (Exception ex) {
			if (command.getErrorReporter() != null) {
				command.getErrorReporter().reportError(this.getClass(), ex, "error executing command: /" + command.getName() + " " + String.join(" ", args));
			} else {
				ex.printStackTrace();
			}
			abstractSender.sendMessage("§cThe command execution failed. Please contact an administrator.");
		}
	}

	public List<String> onTabComplete(T sender, String[] args) {
		AbstractCommandSender<T> abstractSender = abstractSender(sender);
		try {
			return command.autocomplete(abstractSender, args);
		} catch (Exception ex) {
			if (command.getErrorReporter() != null) {
				command.getErrorReporter().reportError(this.getClass(), ex, "error autocompleting command: /" + command.getName() + " " + String.join(" ", args));
			} else {
				ex.printStackTrace();
			}
			abstractSender.sendMessage("§cSomething went wrong. Please contact an administrator.");
			return Collections.emptyList();
		}
	}

	public CommandRoot<?> getCommand() {
		return command;
	}

	public abstract AbstractCommandSender<T> abstractSender(T sender);

	public abstract CommandArguments getArguments(String[] args);
}