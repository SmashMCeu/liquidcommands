package de.liquiddev.command;

import de.liquiddev.command.identity.resolver.McIdentityResolver;
import de.liquiddev.command.identity.resolver.MojangIdentityResolver;
import lombok.Getter;
import lombok.Setter;

public class Commands {

	@Getter
	@Setter
	private static String defaultPrefix = "§bLiquidCommand §8» ";
	@Getter
	@Setter
	private static McIdentityResolver identityResolver = new MojangIdentityResolver();
}
