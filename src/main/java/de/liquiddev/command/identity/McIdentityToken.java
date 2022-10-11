package de.liquiddev.command.identity;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import de.liquiddev.command.Commands;
import de.liquiddev.command.identity.resolver.McIdentityResolver;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class McIdentityToken {

	public static McIdentityToken ofName(String name) {
		return new McIdentityToken(name, null);
	}

	public static McIdentityToken ofUuid(UUID uuid) {
		return new McIdentityToken(null, uuid);
	}

	private Consumer<McIdentity> successConsumer;
	private Consumer<McIdentityToken> failConsumer;

	private final String name;
	private final UUID uuid;

	private McIdentityResolver resolver = Commands.getIdentityResolver();
	private volatile boolean resolving;

	protected void fail() {
		failConsumer.accept(this);
	}

	protected void success(McIdentity identity) {
		successConsumer.accept(identity);
	}

	public Object getIdentifier() {
		return isIdentifiedByUuid() ? uuid : name;
	}

	public boolean isIdentifiedByName() {
		return name != null;
	}

	public boolean isIdentifiedByUuid() {
		return uuid != null;
	}

	public McIdentityToken whenFound(Consumer<McIdentity> identityConsumer) {
		if (successConsumer != null) {
			throw new IllegalStateException("whenFound action already set.");
		}
		successConsumer = identityConsumer;
		this.resolve();
		return this;
	}

	public <T> McIdentityToken whenFound(BiConsumer<McIdentity, T> function, T param) {
		return whenFound(identity -> function.accept(identity, param));
	}

	public McIdentityToken whenUnknown(Consumer<McIdentityToken> unknownTokenConsumer) {
		failConsumer = unknownTokenConsumer;
		return this;
	}

	public McIdentityToken withResolver(McIdentityResolver resolver) {
		this.resolver = resolver;
		return this;
	}

	@Synchronized
	private McIdentityToken resolve() {
		if (resolving) {
			throw new IllegalStateException("Token already resolved.");
		}
		resolving = true;
		if (isIdentifiedByName()) {
			resolver.findByName(name)
					.thenAccept(idOptional -> {
						success(idOptional.get());
					})
					.exceptionally(ex -> {
						fail();
						return null;
					});
		} else if (isIdentifiedByUuid()) {
			resolver.findByUuid(uuid)
					.thenAccept(idOptional -> {
						success(idOptional.get());
					})
					.exceptionally(ex -> {
						fail();
						return null;
					});
		} else {
			throw new IllegalStateException();
		}
		return this;
	}
}
