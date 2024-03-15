package xyz.zlatanov.subsbuddy.connector.translation;

import org.springframework.stereotype.Service;

import xyz.zlatanov.subsbuddy.domain.Language;

@Service
public class GoogleTranslateTranslationConnector implements TranslationConnector {

	@Override
	public String translate(String text, Language from, Language to) {
		// todo
		return text.toUpperCase();
	}
}
