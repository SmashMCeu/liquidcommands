package de.liquiddev.command.example;

import java.util.IllegalFormatException;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.CommandArguments;
import de.liquiddev.command.CommandChild;
import de.liquiddev.command.CommandFailException;
import de.liquiddev.command.CommandVisibility;

@SuppressWarnings("rawtypes")
public class VersionCommand extends CommandChild {

	public static VersionCommandBuilder newBuilder() {
		return new VersionCommandBuilder();
	}

	public static class VersionCommandBuilder {
		private String prefix;
		private String format = "§a%s §7version §a%s §7by %s";
		private String version;
		private String pluginName = "Plugin";
		private String author = "unknown";
		private CommandVisibility visibility = CommandVisibility.HIDE_HELP;

		public VersionCommandBuilder prefix(String prefix) {
			this.prefix = prefix;
			return this;
		}

		public VersionCommandBuilder format(String format) {
			this.format = format;
			return this;
		}

		public VersionCommandBuilder version(String version) {
			this.version = version;
			return this;
		}

		public VersionCommandBuilder pluginName(String pluginName) {
			this.pluginName = pluginName;
			return this;
		}

		public VersionCommandBuilder author(String author) {
			this.author = author;
			return this;
		}

		public VersionCommandBuilder visibility(CommandVisibility visibility) {
			this.visibility = visibility;
			return this;
		}

		public VersionCommand create() {
			if (version == null) {
				throw new IllegalStateException("version must be specified");
			}
			try {
				String.format(format, pluginName, version, author);
			} catch (IllegalFormatException ex) {
				ex.fillInStackTrace();
				throw ex;
			}
			return new VersionCommand(pluginName, author, prefix, format, version, visibility);
		}
	}

	private String prefix;
	private String format;
	private String version;
	private String pluginName;
	private String author;

	VersionCommand(String pluginName, String author, String prefix, String format, String version, CommandVisibility visibility) {
		super(Object.class, "version", "");
		this.addAlias("ver");
		this.addAlias("v");
		this.pluginName = pluginName;
		this.author = author;
		this.prefix = prefix;
		this.format = format;
		this.version = version;
		this.setVisibility(visibility);
	}

	@Override
	public void onCommand(AbstractCommandSender sender, CommandArguments args) throws CommandFailException {
		StringBuilder str = new StringBuilder();
		if (this.prefix != null) {
			str.append(prefix);
		}
		str.append(String.format(format, pluginName, version, author));
		sender.sendMessage(str.toString());
	}
}
