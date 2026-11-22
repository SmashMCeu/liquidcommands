package de.liquiddev.command.identity.resolver;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import de.liquiddev.command.identity.McIdentity;

public interface McIdentityResolver {

	CompletableFuture<Optional<McIdentity>> findByName(String name);

	CompletableFuture<Optional<McIdentity>> findByUuid(UUID uuid);

	/**
	 * Finds the names of players whose name starts with the given prefix.
	 * <p>
	 * Implementations are expected to perform their lookup asynchronously and
	 * complete the returned future on a background thread. Autocompletion is
	 * requested on the main server thread, so the returned future must never be
	 * awaited there.
	 * <p>
	 * The default implementation returns an empty list, which keeps resolvers that
	 * do not support prefix lookups working.
	 *
	 * @param prefix prefix the player names must start with
	 * @param limit  maximum number of names to return
	 * @return future completing with the matching names, possibly empty
	 */
	default CompletableFuture<List<String>> findNamesByPrefix(String prefix, int limit) {
		return CompletableFuture.completedFuture(Collections.emptyList());
	}

}
