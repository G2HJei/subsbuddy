package xyz.zlatanov.subsbuddy.connector.translation;

import xyz.zlatanov.subsbuddy.domain.Language;

public interface TranslationConnector {

	String translate(String text, Language from, Language to);
}
