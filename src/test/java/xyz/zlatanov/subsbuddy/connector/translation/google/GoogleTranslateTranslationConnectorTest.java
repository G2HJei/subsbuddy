package xyz.zlatanov.subsbuddy.connector.translation.google;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import feign.FeignException;
import org.junit.jupiter.api.Test;

import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.connector.translation.google.client.GoogleTranslateRestClient;

class GoogleTranslateTranslationConnectorTest {

	public static final String	LOREM_IPSUM	= "Lorem. Ipsum.";
	public static final String	FOO_BAR		= "Foo. Bar.";
	GoogleTranslateRestClient	restClient	= mock(GoogleTranslateRestClient.class);
	TranslationConnector		connector	= new GoogleTranslateTranslationConnector(restClient, "mock");

	@Test
	void shouldTranslate() {
		when(restClient.translate("mock", "Foo", "en", "bg")).thenReturn("Bar");
		assertEquals("Bar", connector.translate("Foo", EN, BG));
	}

	@Test
	void shouldTranslateMultipleSentences() {
		when(restClient.translate("mock", FOO_BAR, "en", "bg"))
				.thenReturn(LOREM_IPSUM);
		assertEquals(LOREM_IPSUM, connector.translate(FOO_BAR, EN, BG));
	}

	@Test
	void shouldKeepEnglishTextInCaseOfError() {
		when(restClient.translate("mock", FOO_BAR, "en", "bg"))
				.thenThrow(FeignException.class);
		assertEquals(FOO_BAR, connector.translate(FOO_BAR, EN, BG));
	}

	@Test
	void shouldKeepEnglishTextInCaseOfLongTranslation() {
		when(restClient.translate("mock", FOO_BAR, "en", "bg"))
				.thenReturn(LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM
						+ LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM
						+ LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM + LOREM_IPSUM);
		assertEquals(FOO_BAR, connector.translate(FOO_BAR, EN, BG));
	}

}