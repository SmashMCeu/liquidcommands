package de.liquiddev.command.identity.resolver;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import de.liquiddev.command.identity.McIdentity;

public class MojangIdentityResolver implements McIdentityResolver {

	@Override
	public CompletableFuture<Optional<McIdentity>> findByName(String name) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public CompletableFuture<Optional<McIdentity>> findByUuid(UUID uuid) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
