package de.liquiddev.command.example;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Preconditions;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandNode;
import de.liquiddev.command.CommandPermissionException;
import de.liquiddev.command.CommandVisibility;
import de.liquiddev.command.InvalidCommandArgException;

@SuppressWarnings("rawtypes")
public class HelpCommand extends CommandChild {

	public static HelpCommandBuilder newBuilder(CommandNode<?> command) {
		return new HelpCommandBuilder(command);
	}

	public static class HelpCommandBuilder {

		private final CommandNode<?> command;
		private String header;
		private String footer;
		private String format;
		private String descriptionFormat;
		private boolean includeSelf = false;
		private boolean checkPermission = true;
		private List<String> customPath = new ArrayList<>(0);

		public HelpCommandBuilder(CommandNode<?> command) {
			this.command = command;

			/* Default style */
			String name = command.getName();
			name = name.substring(0, 1)
					.toUpperCase() + name.substring(1);
			defaultFormat(name, "§3", "§b");
		}

		public HelpCommandBuilder defaultFormat(String commandHelpName, String darkColor, String lightColor) {
			header = "§7§m|一一一一一一一一一一一" + darkColor + "§l " + commandHelpName + " §7§m一一一一一一一一一一一|§r\n ";
			footer = " ";
			format = " §8● " + lightColor + "%s §f%s";
			descriptionFormat = " §8- §7%s";
			return this;
		}

		public HelpCommandBuilder header(String header) {
			this.header = header;
			return this;
		}

		public HelpCommandBuilder footer(String footer) {
			this.footer = footer;
			return this;
		}

		public HelpCommandBuilder format(String format) {
			this.format = format;
			return this;
		}

		public HelpCommandBuilder descriptionFormat(String descriptionFormat) {
			this.descriptionFormat = descriptionFormat;
			return this;
		}

		public HelpCommandBuilder includeSelf(boolean includeSelf) {
			this.includeSelf = includeSelf;
			return this;
		}

		public HelpCommandBuilder checkPermission(boolean checkPermission) {
			this.checkPermission = checkPermission;
			return this;
		}

		public HelpCommandBuilder addPath(String path) {
			this.customPath.add(path);
			return this;
		}

		public HelpCommand create() {
			return new HelpCommand(command, includeSelf, checkPermission, header, footer, format, descriptionFormat, customPath);
		}
	}

	private final CommandNode<?> command;
	private boolean includeSelf;
	private boolean checkPermission;
	private String header;
	private String footer;
	private String format;
	private String descriptionFormat;
	private List<String> customPathes;

	HelpCommand(CommandNode<?> commandToHelp, boolean includeSelf, boolean checkPermission, String header, String footer, String format, String descFormat, List<String> customPath) {
		super(Object.class, "help", "");
		Preconditions.checkNotNull(commandToHelp, "commandToHelp must not be null");
		Preconditions.checkNotNull(header, "header must not be null");
		Preconditions.checkNotNull(footer, "footer must not be null");
		Preconditions.checkNotNull(format, "format must not be null");
		Preconditions.checkNotNull(descFormat, "descFormat must not be null");
		this.addAlias("h");
		this.addAlias("?");
		this.command = commandToHelp;
		this.includeSelf = includeSelf;
		this.checkPermission = checkPermission;
		this.header = header;
		this.footer = footer;
		this.format = format;
		this.descriptionFormat = descFormat;
		this.customPathes = customPath;
		/* help command should be first in list */
		this.setVisibility(CommandVisibility.HIDE_HELP);
	}

	@Override
	public void onCommand(AbstractCommandSender sender, CommandArguments args) throws InvalidCommandArgException, CommandPermissionException {
		StringBuilder message = new StringBuilder();
		appendHeader(message);
		message.append("\n");

		/* help command should be first in list */
		if (includeSelf) {
			appendHelp(message, this.command);
			message.append("\n");
		}

		for (CommandNode<?> cmd : command.getSubCommands(0)) {
			if (!cmd.isHelpVisible()) {
				continue;
			}
			if (!checkPermission || cmd.hasPermission(sender)) {
				appendHelp(message, cmd);
				message.append("\n");
			}
		}

		/* Custom pathes */
		for (String path : customPathes) {
			appendHelp(message, command.getAbsoluteName(), path, null);
			message.append("\n");
		}
		appendFooter(message);
		sender.sendMessage(message.toString());
	}

	/**
	 * Override if a sender has permission to help command if something would show
	 * in help list.
	 */
	@Override
	public boolean hasPermission(AbstractCommandSender sender) {
		boolean anyPermission = !checkPermission;
		if (checkPermission) {
			for (CommandNode<?> cmd : command.getSubCommands(0)) {
				if (cmd.isHelpVisible() && cmd.hasPermission(sender)) {
					anyPermission = true;
					break;
				}
			}
		} else {
			anyPermission = true;
		}
		return anyPermission && super.hasPermission(sender);
	}

	protected void appendHeader(StringBuilder str) {
		str.append(header);
	}

	protected void appendFooter(StringBuilder str) {
		str.append(footer);
	}

	protected void appendHelp(StringBuilder str, CommandNode<?> subCommand) {
		this.appendHelp(str, subCommand.getAbsoluteName(), subCommand.getHint(), subCommand.getDescription());
	}

	protected void appendHelp(StringBuilder str, String absoluteName, String hint, String description) {
		String help = String.format(format, absoluteName, hint);
		str.append(help);

		if (description != null) {
			String desc = String.format(descriptionFormat, description);
			str.append(desc);
		}
	}
}
