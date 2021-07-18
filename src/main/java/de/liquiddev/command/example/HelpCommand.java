package de.liquiddev.command.example;

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
		private String header = "§8╔═════════════════════════╗\n";
		private String footer = "\n§8╚═════════════════════════╝";
		private String format = " §8● §b%s §f%s";
		private String descriptionFormat = " §8- §7%s";
		private boolean includeSelf = false;
		private boolean checkPermission = true;

		public HelpCommandBuilder(CommandNode<?> command) {
			this.command = command;
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

		public HelpCommand create() {
			return new HelpCommand(command, includeSelf, checkPermission, header, footer, format, descriptionFormat);
		}
	}

	private final CommandNode<?> command;
	private boolean includeSelf;
	private boolean checkPermission;
	private String header;
	private String footer;
	private String format;
	private String descriptionFormat;

	HelpCommand(CommandNode<?> commandToHelp, boolean includeSelf, boolean checkPermission, String header, String footer, String format, String descFormat) {
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
		String help = String.format(format, subCommand.getAbsoluteName(), subCommand.getHint());
		str.append(help);

		if (subCommand.getDescription() != null) {
			String desc = String.format(descriptionFormat, subCommand.getDescription());
			str.append(desc);
		}
	}
}
