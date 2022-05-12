package de.liquiddev.command.identity.resolver;

import java.util.Optional;
import java.util.UUID;

import de.liquiddev.command.identity.McIdentity;

public interface McIdentityResolver {

	Optional<McIdentity> findByName(String name);

	Optional<McIdentity> findByUuid(UUID uuid);

}
