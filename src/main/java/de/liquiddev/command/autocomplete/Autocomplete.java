package de.liquiddev.command.autocomplete;

import de.liquiddev.command.Commands;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Autocomplete {

	@SuppressWarnings("rawtypes")
	public static final Autocompleter NONE = (t, s) -> Collections.emptyList();

	protected Autocomplete() {
	}

	public static <T> Autocompleter<T> text(String... text) {
		return new TextAutocompleter<T>(text);
	}

	public static <T> Autocompleter<T> text(List<String> text) {
		return new TextAutocompleter<T>(text);
	}

	public static <T> Autocompleter<T> array(Collection<T> array) {
		return new StatelessAutocompleter<>(() -> array);
	}

	public static <T, R> Autocompleter<T> mapped(Collection<R> array, Function<R, String> mapFunction) {
		return new StatelessAutocompleter<>(() -> array, mapFunction);
	}

	public static <T, R> Autocompleter<T> supply(Supplier<Collection<R>> supplier) {
		return new StatelessAutocompleter<>(supplier);
	}

	public static <T, R> Autocompleter<T> supplyMapped(Supplier<Collection<R>> supplier, Function<R, String> mapFunction) {
		return new StatelessAutocompleter<>(supplier, mapFunction);
	}

	public static <T> Autocompleter<T> combine(Autocompleter<? super T>... completors) {
		CombiningAutocompleter<T> combined = new CombiningAutocompleter<>(false);
		for (Autocompleter<? super T> all : completors) {
			combined.addAutocompleter(all);
		}
		return combined;
	}

	@SuppressWarnings("rawtypes")
	public static <T> Autocompleter<T> players() {
		return players(true, false);
	}

	public static <T> Autocompleter<T> otherPlayers() {
		return players(true, true);
	}

	public static <T> Autocompleter<T> players(boolean hideInvisiblePlayers, boolean excludeSelf) {
		return new PlayerAutocompleter(hideInvisiblePlayers, excludeSelf);
	}

	@SuppressWarnings("rawtypes")
	public static <T> Autocompleter<T> proxyPlayers() {
		return new ProxyPlayerAutocompleter();
	}

	/**
	 * Creates an autocompleter that suggests all known players: players that are
	 * currently online plus offline players whose name matches the input.
	 * <p>
	 * The platform is detected once when this factory is called: BungeeCord
	 * players are used on proxies, Bukkit players on servers, and no online
	 * players when neither is present. Online players are re-read on every
	 * autocomplete request, so changes to the online player list are reflected
	 * immediately.
	 * <p>
	 * Offline names are requested from the identity resolver configured in
	 * {@link Commands} and cached locally. Offline lookups only happen for
	 * prefixes of at least three characters; shorter prefixes complete online
	 * players only. All identity lookups are asynchronous, so autocompletion
	 * never blocks the server thread.
	 *
	 * @param <T> type of sender who requested the autocompletion
	 * @return autocompleter for online and offline players
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Autocompleter<T> allPlayers() {
		Autocompleter online;
		if (isClassPresent("net.md_5.bungee.api.ProxyServer")) {
			online = proxyPlayers();
		} else if (isClassPresent("org.bukkit.Bukkit")) {
			online = players();
		} else {
			online = none();
		}
		return new AllPlayersAutocompleter(online, Commands::getIdentityResolver);
	}

	private static boolean isClassPresent(String className) {
		try {
			Class.forName(className, false, Autocomplete.class.getClassLoader());
			return true;
		} catch (ClassNotFoundException | LinkageError ex) {
			return false;
		}
	}

	public static <T> Autocompleter<T> none() {
		return NONE;
	}
}