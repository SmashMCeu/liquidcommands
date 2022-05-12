package de.liquiddev.command.identity.resolver;

import java.util.Optional;
import java.util.UUID;

import de.liquiddev.command.identity.McIdentity;

public class MojangIdentityResolver implements McIdentityResolver {

	@Override
	public Optional<McIdentity> findByName(String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public Optional<McIdentity> findByUuid(UUID uuid) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
