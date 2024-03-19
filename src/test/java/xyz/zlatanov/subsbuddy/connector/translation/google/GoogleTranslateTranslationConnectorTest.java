package xyz.zlatanov.subsbuddy.connector.translation.google;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.connector.translation.google.client.GoogleTranslateRestClient;

class GoogleTranslateTranslationConnectorTest {

	GoogleTranslateRestClient	restClient	= mock(GoogleTranslateRestClient.class);
	TranslationConnector		connector	= new GoogleTranslateTranslationConnector(restClient, "mock");

	@Test
	void translate_sentence_returns() {
		when(restClient.translate("mock","Foo", "en", "bg")).thenReturn("Bar");
		assertEquals("Bar", connector.translate("Foo", EN, BG));
	}

	@Test
	void translate_multipleSentences_returns() {
		when(restClient.translate("mock", "Foo. Bar.", "en", "bg"))
				.thenReturn("Lorem. Ipsum.");
		assertEquals("Lorem. Ipsum.", connector.translate("Foo. Bar.", EN, BG));
	}

}