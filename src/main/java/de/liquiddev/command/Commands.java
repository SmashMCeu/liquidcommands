package de.liquiddev.command;

public class Commands {

	private static volatile String defaultPrefix = "§bLiquidCommand §8» ";

	public static String getDefaultPrefix() {
		System.out.println("returning default prefix " + defaultPrefix);
		System.out.println("classpath from commands: " + Commands.class.getName());
		return defaultPrefix;
	}

	public static void setDefaultPrefix(String prefix) {
		System.out.println("set default prefix to " + prefix);
		defaultPrefix = prefix;
	}
}
