package xyz.zlatanov.subsbuddy.query.translatetext;

import xyz.zlatanov.subsbuddy.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.domain.Language;

public class CapitalizingTranslationConnector implements TranslationConnector {

	@Override
	public String translate(String text, Language from, Language to, String context) {
		return text.toUpperCase();
	}
}
