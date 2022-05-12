package de.liquiddev.command.identity;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.logging.log4j.util.TriConsumer;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

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
		return this;
	}

	public <T> McIdentityToken whenFound(BiConsumer<McIdentity, T> function, T param) {
		return whenFound(identity -> function.accept(identity, param));
	}

	public <T, U> McIdentityToken whenFound(TriConsumer<McIdentity, T, U> function, T param1, U param2) {
		return whenFound(identity -> function.accept(identity, param1, param2));
	}

	public McIdentityToken whenUnknown(Consumer<McIdentityToken> unknownTokenConsumer) {
		failConsumer = unknownTokenConsumer;
		return this;
	}
}
