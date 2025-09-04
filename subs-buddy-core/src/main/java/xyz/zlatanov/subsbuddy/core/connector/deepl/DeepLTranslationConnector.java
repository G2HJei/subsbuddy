package xyz.zlatanov.subsbuddy.core.connector.deepl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.deepl.api.*;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.domain.Language;
import xyz.zlatanov.subsbuddy.core.domain.exception.SubsBuddyException;

public class DeepLTranslationConnector implements TranslationConnector {

	private final List<DeepLClient> clients;

	public DeepLTranslationConnector(List<String> apiKeys) {
		this.clients = apiKeys.stream()
				.map(DeepLClient::new)
				.toList();
		usagePercent();
	}

	@Override
	@SneakyThrows
	public long usagePercent() {
		val chars = clients.stream()
				.map(this::getChars)
				.toList();
		return chars.stream()
				.filter(Objects::nonNull)
				.map(this::calcPercent)
				.reduce(0L, Long::sum) / chars.size();
	}

	private long calcPercent(Usage.Detail ch) {
		return (ch.getCount() * 100) / ch.getLimit();
	}

	@SneakyThrows
	private Usage.Detail getChars(DeepLClient deepLClient) {
		val usage = deepLClient.getUsage();
		assert usage != null;
		return usage.getCharacter();
	}

	@Override
	public String translate(String text, Language from, Language to, String context) {
		val availableClients = new ArrayList<>(clients);
		val options = new TextTranslationOptions().setContext(context);
		val translationItem = new TranslationItem(availableClients, text, from, to, options, 0);
		val translatedText = runTranslation(translationItem);
		return translatedText;
	}

	@SneakyThrows
	private String runTranslation(TranslationItem item) {
		delayRetry(item.retryCount);
		try {
			return item.runClients.getFirst()
					.translateText(item.text, item.from.name(), item.to.name(), item.options).getText();
		} catch (QuotaExceededException e) {
			verifyRemaining(item.runClients);
			return runTranslation(item);
		} catch (DeepLException e) {
			item.retryCount++;
			return runTranslation(item);
		}
	}

	private void verifyRemaining(List<DeepLClient> clients) {
		if (clients.size() > 1) {
			clients.removeFirst();
		} else {
			throw new SubsBuddyException("Ran out of translation quota.");
		}
	}

	@SneakyThrows
	private void delayRetry(long retryCount) {
		if (retryCount > 3) {
			throw new SubsBuddyException("Translation retry limit exceeded");
		} else {
			Thread.sleep(retryCount * 2_000);
		}
	}

	@AllArgsConstructor
	private static final class TranslationItem {

		private List<DeepLClient>		runClients;
		private String					text;
		private Language				from;
		private Language				to;
		private TextTranslationOptions	options;
		private int						retryCount;
	}
}
