package de.liquiddev.command;

public class Commands {

	private static volatile String defaultPrefix = "§bLiquidCommand §8» ";

	public static String getDefaultPrefix() {
		return defaultPrefix;
	}

	public static void setDefaultPrefix(String prefix) {
		defaultPrefix = prefix;
	}
}
