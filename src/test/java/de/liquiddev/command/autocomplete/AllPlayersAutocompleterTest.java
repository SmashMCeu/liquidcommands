package de.liquiddev.command.autocomplete;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import de.liquiddev.command.AbstractCommandSender;
import de.liquiddev.command.identity.resolver.McIdentityResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AllPlayersAutocompleterTest {

	private static final int OFFLINE_FETCH_LIMIT = 50;

	private McIdentityResolver resolver;
	private AbstractCommandSender<Object> sender;

	@BeforeEach
	@SuppressWarnings("unchecked")
	public void setUp() {
		AllPlayersAutocompleter.clearCache();
		this.resolver = mock(McIdentityResolver.class);
		this.sender = mock(AbstractCommandSender.class);
	}

	@Test
	public void emptyPrefixReturnsOnlineNamesWithoutIdentityLookup() {
		AllPlayersAutocompleter<Object> completer = completer(online("Liquid", "LiquidDev"));

		Collection<String> result = completer.autocomplete(sender, "");

		assertEquals(List.of("Liquid", "LiquidDev"), new ArrayList<>(result));
		verify(resolver, never()).findNamesByPrefix(anyString(), anyInt());
	}

	@Test
	public void fullOnlinePageSkipsIdentityLookup() {
		List<String> names = IntStream.range(0, 12)
				.mapToObj(i -> "Player" + i)
				.collect(Collectors.toList());
		AllPlayersAutocompleter<Object> completer = completer(online(names.toArray(new String[0])));

		Collection<String> result = completer.autocomplete(sender, "p");

		assertEquals(names.subList(0, 10), new ArrayList<>(result));
		verify(resolver, never()).findNamesByPrefix(anyString(), anyInt());
	}

	@Test
	public void pendingLookupReturnsOnlineAndAddsOfflineNamesOnceCompleted() {
		CompletableFuture<List<String>> future = new CompletableFuture<>();
		when(resolver.findNamesByPrefix("l", OFFLINE_FETCH_LIMIT)).thenReturn(future);
		AllPlayersAutocompleter<Object> completer = completer(online("Liquid"));

		Collection<String> pending = completer.autocomplete(sender, "l");

		assertEquals(List.of("Liquid"), new ArrayList<>(pending));

		future.complete(List.of("LiquidOffline"));
		Collection<String> completed = completer.autocomplete(sender, "l");

		assertEquals(List.of("Liquid", "LiquidOffline"), new ArrayList<>(completed));
	}

	@Test
	public void completedShorterResponseAnswersLongerPrefixesLocally() {
		when(resolver.findNamesByPrefix("liqui", OFFLINE_FETCH_LIMIT))
				.thenReturn(CompletableFuture.completedFuture(List.of("Liquid")));
		AllPlayersAutocompleter<Object> completer = completer(online());

		assertEquals(List.of("Liquid"), new ArrayList<>(completer.autocomplete(sender, "liqui")));
		assertEquals(List.of("Liquid"), new ArrayList<>(completer.autocomplete(sender, "liquid")));
		assertTrue(completer.autocomplete(sender, "liquidd").isEmpty());
		verify(resolver, times(1)).findNamesByPrefix(anyString(), anyInt());
	}

	@Test
	public void saturatedResponseTriggersLookupForLongerPrefix() {
		List<String> saturated = IntStream.range(0, OFFLINE_FETCH_LIMIT)
				.mapToObj(i -> "aPlayer" + i)
				.collect(Collectors.toList());
		when(resolver.findNamesByPrefix("a", OFFLINE_FETCH_LIMIT))
				.thenReturn(CompletableFuture.completedFuture(saturated));
		when(resolver.findNamesByPrefix("ab", OFFLINE_FETCH_LIMIT))
				.thenReturn(CompletableFuture.completedFuture(List.of("abPlayer")));
		AllPlayersAutocompleter<Object> completer = completer(online());

		assertEquals(10, completer.autocomplete(sender, "a").size());
		assertEquals(List.of("abPlayer"), new ArrayList<>(completer.autocomplete(sender, "ab")));
		verify(resolver).findNamesByPrefix("a", OFFLINE_FETCH_LIMIT);
		verify(resolver).findNamesByPrefix("ab", OFFLINE_FETCH_LIMIT);
	}

	@Test
	public void offlineDuplicatesOfOnlinePlayersAreRemoved() {
		when(resolver.findNamesByPrefix("l", OFFLINE_FETCH_LIMIT))
				.thenReturn(CompletableFuture.completedFuture(List.of("liquid")));
		AllPlayersAutocompleter<Object> completer = completer(online("Liquid"));

		Collection<String> result = completer.autocomplete(sender, "l");

		assertEquals(List.of("Liquid"), new ArrayList<>(result));
	}

	@Test
	public void combinedSuggestionsAreCappedAtTen() {
		List<String> offline = IntStream.range(0, 10)
				.mapToObj(i -> "Liquid" + i)
				.collect(Collectors.toList());
		when(resolver.findNamesByPrefix("l", OFFLINE_FETCH_LIMIT))
				.thenReturn(CompletableFuture.completedFuture(offline));
		AllPlayersAutocompleter<Object> completer = completer(online("LiquidA", "LiquidB"));

		Collection<String> result = completer.autocomplete(sender, "l");

		assertEquals(List.of("LiquidA", "LiquidB", "Liquid0", "Liquid1", "Liquid2", "Liquid3", "Liquid4", "Liquid5",
				"Liquid6", "Liquid7"), new ArrayList<>(result));
	}

	@Test
	public void repeatedRequestsWhileInFlightUseOneIdentityLookup() {
		CompletableFuture<List<String>> future = new CompletableFuture<>();
		when(resolver.findNamesByPrefix("l", OFFLINE_FETCH_LIMIT)).thenReturn(future);
		AllPlayersAutocompleter<Object> completer = completer(online());

		completer.autocomplete(sender, "l");
		completer.autocomplete(sender, "l");

		verify(resolver, times(1)).findNamesByPrefix("l", OFFLINE_FETCH_LIMIT);
	}

	private AllPlayersAutocompleter<Object> completer(Autocompleter<? super Object> online) {
		return new AllPlayersAutocompleter<>(online, () -> resolver);
	}

	private static Autocompleter<Object> online(String... names) {
		return (sender, prefix) -> Arrays.asList(names);
	}
}
