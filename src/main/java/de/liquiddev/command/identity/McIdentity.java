package de.liquiddev.command.identity;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

public interface McIdentity {

	public static McIdentity snapshot(org.bukkit.entity.Player bukkitPlayer) {
		return new McIdentitySnapshot(bukkitPlayer.getUniqueId(), bukkitPlayer.getName(), true);
	}

	public static McIdentity snapshot(net.md_5.bungee.api.connection.ProxiedPlayer bungeePlayer) {
		return new McIdentitySnapshot(bungeePlayer.getUniqueId(), bungeePlayer.getName(), true);
	}

	public static McIdentity offline(UUID uuid, String name) {
		return new McIdentitySnapshot(uuid, name, false);
	}

	UUID getUuid();

	String getName();

	boolean isOnline();

	@Getter
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	@EqualsAndHashCode(of = "uuid")
	@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
	public static class McIdentitySnapshot implements McIdentity {

		UUID uuid;

		String name;

		boolean online;
	}
}
