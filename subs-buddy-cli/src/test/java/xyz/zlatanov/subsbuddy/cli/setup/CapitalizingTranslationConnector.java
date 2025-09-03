package xyz.zlatanov.subsbuddy.cli.setup;

import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.domain.Language;

public class CapitalizingTranslationConnector implements TranslationConnector {

	@Override
	public long usagePercent() {
		return -1;
	}

	@Override
	public String translate(String text, Language from, Language to, String context) {
		return text.toUpperCase();
	}
}