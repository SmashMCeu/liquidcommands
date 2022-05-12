package de.liquiddev.command.identity.resolver;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import de.liquiddev.command.identity.McIdentity;

public interface McIdentityResolver {

	CompletableFuture<Optional<McIdentity>> findByName(String name);

	CompletableFuture<Optional<McIdentity>> findByUuid(UUID uuid);

}
