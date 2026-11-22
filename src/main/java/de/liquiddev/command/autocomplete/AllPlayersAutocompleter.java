package de.liquiddev.command.autocomplete;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.identity.resolver.McIdentityResolver;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Autocompleter for all players known to the server: currently online players
 * (delegated to another autocompleter) plus offline players matching the input.
 * <p>
 * Offline names are requested from an {@link McIdentityResolver} asynchronously
 * and cached. Offline lookups are only made for prefixes of at least
 * {@value #MIN_OFFLINE_PREFIX_LENGTH} characters; shorter prefixes return
 * online players only. Every lookup only ever inspects already completed
 * futures, so {@link #autocomplete(AbstractCommandSender, String)} never blocks
 * the calling thread.
 *
 * @param <T> type of sender who requested the autocompletion
 */
class AllPlayersAutocompleter<T> implements Autocompleter<T> {

	private static final int OFFLINE_FETCH_LIMIT = 50;
	private static final int MAX_SUGGESTIONS = 25;
	private static final int MIN_OFFLINE_PREFIX_LENGTH = 3;

	private static final Cache<String, CompletableFuture<List<String>>> OFFLINE_CACHE = CacheBuilder.newBuilder()
			.maximumSize(1024)
			.expireAfterWrite(20, TimeUnit.MINUTES)
			.build();

	private final Autocompleter<? super T> online;
	private final Supplier<McIdentityResolver> resolver;

	AllPlayersAutocompleter(Autocompleter<? super T> online, Supplier<McIdentityResolver> resolver) {
		this.online = online;
		this.resolver = resolver;
	}

	/**
	 * Clears all cached offline lookups. Intended for tests.
	 */
	static void clearCache() {
		OFFLINE_CACHE.invalidateAll();
	}

	@Override
	public Collection<String> autocomplete(AbstractCommandSender<? extends T> sender, String prefix) {
		if (prefix == null) {
			return Collections.emptyList();
		}
		Collection<String> onlineNames = online.autocomplete(sender, prefix);
		if (prefix.isEmpty()) {
			return onlineNames;
		}
		if (prefix.length() < MIN_OFFLINE_PREFIX_LENGTH || onlineNames.size() >= MAX_SUGGESTIONS) {
			return firstSuggestions(onlineNames);
		}
		Map<String, String> merged = new LinkedHashMap<>();
		merge(merged, onlineNames, prefix);
		merge(merged, offlineNames(prefix), prefix);
		return firstSuggestions(merged.values());
	}

	private static List<String> firstSuggestions(Collection<String> names) {
		List<String> suggestions = new ArrayList<>(MAX_SUGGESTIONS);
		for (String name : names) {
			if (suggestions.size() >= MAX_SUGGESTIONS) {
				break;
			}
			suggestions.add(name);
		}
		return suggestions;
	}

	private static void merge(Map<String, String> target, Collection<String> names, String prefix) {
		for (String name : names) {
			if (name == null || !name.regionMatches(true, 0, prefix, 0, prefix.length())) {
				continue;
			}
			target.putIfAbsent(name.toLowerCase(Locale.ROOT), name);
		}
	}

	private static List<String> filterByPrefix(Collection<String> names, String prefix) {
		List<String> filtered = new ArrayList<>();
		for (String name : names) {
			if (name != null && name.regionMatches(true, 0, prefix, 0, prefix.length())) {
				filtered.add(name);
			}
		}
		return filtered;
	}

	/**
	 * Returns the cached offline names for the given prefix without ever waiting
	 * for a pending lookup.
	 * <p>
	 * A completed lookup for a shorter prefix that returned less than
	 * {@link #OFFLINE_FETCH_LIMIT} names is authoritative for every longer prefix:
	 * the response was not saturated, so no longer prefix can have additional
	 * matches. Such an entry is used to answer locally and no request is sent.
	 *
	 * @param prefix prefix to find offline names for
	 * @return currently known offline names matching the prefix, possibly empty
	 */
	private List<String> offlineNames(String prefix) {
		String key = prefix.toLowerCase(Locale.ROOT);
		String ancestorKey = findCompleteAncestor(key);
		if (ancestorKey != null) {
			CompletableFuture<List<String>> ancestor = OFFLINE_CACHE.getIfPresent(ancestorKey);
			if (ancestor != null) {
				return filterByPrefix(ancestor.getNow(Collections.emptyList()), prefix);
			}
		}
		CompletableFuture<List<String>> future = OFFLINE_CACHE.asMap().computeIfAbsent(key, k -> query(prefix));
		if (future == null) {
			return Collections.emptyList();
		}
		return future.getNow(Collections.emptyList());
	}

	private static String findCompleteAncestor(String key) {
		String ancestorKey = null;
		for (Map.Entry<String, CompletableFuture<List<String>>> entry : OFFLINE_CACHE.asMap().entrySet()) {
			String candidate = entry.getKey();
			if (candidate.length() >= key.length() || !key.startsWith(candidate)) {
				continue;
			}
			if (!isComplete(entry.getValue())) {
				continue;
			}
			if (ancestorKey == null || candidate.length() > ancestorKey.length()) {
				ancestorKey = candidate;
			}
		}
		return ancestorKey;
	}

	private static boolean isComplete(CompletableFuture<List<String>> future) {
		if (!future.isDone() || future.isCompletedExceptionally()) {
			return false;
		}
		try {
			return future.getNow(Collections.emptyList()).size() < OFFLINE_FETCH_LIMIT;
		} catch (CancellationException ex) {
			return false;
		}
	}

	private CompletableFuture<List<String>> query(String prefix) {
		try {
			McIdentityResolver identityResolver = resolver.get();
			if (identityResolver == null) {
				return CompletableFuture.completedFuture(Collections.emptyList());
			}
			CompletableFuture<List<String>> future = identityResolver.findNamesByPrefix(prefix, OFFLINE_FETCH_LIMIT);
			if (future == null) {
				return CompletableFuture.completedFuture(Collections.emptyList());
			}
			return future.exceptionally(ex -> Collections.emptyList());
		} catch (RuntimeException ex) {
			return CompletableFuture.completedFuture(Collections.emptyList());
		}
	}

}
