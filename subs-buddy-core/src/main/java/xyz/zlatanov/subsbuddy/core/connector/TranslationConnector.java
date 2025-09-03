package xyz.zlatanov.subsbuddy.core.connector;

import xyz.zlatanov.subsbuddy.core.domain.Language;

public interface TranslationConnector {

	long usagePercent();

	default String translate(String text, Language from, Language to) {
		return translate(text, from, to, "");
	}

	String translate(String text, Language from, Language to, String context);
}
