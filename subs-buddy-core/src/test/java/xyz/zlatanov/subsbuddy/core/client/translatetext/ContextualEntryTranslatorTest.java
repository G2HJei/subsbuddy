package xyz.zlatanov.subsbuddy.core.client.translatetext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.argumentSet;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static xyz.zlatanov.subsbuddy.core.TestUtils.entries;
import static xyz.zlatanov.subsbuddy.core.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.core.domain.Language.EN;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.domain.Language;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

@ExtendWith(MockitoExtension.class)
class ContextualEntryTranslatorTest {

	@Spy
	CapitalizingTranslationConnector	connector;
	@InjectMocks
	ContextualEntryTranslator			translator;

	@ParameterizedTest
	@MethodSource("subsEntriesArgs")
	void shouldTranslate(List<SubtitleEntry> givenEntries, List<SubtitleEntry> expectedEntries) {
		val actualEntries = translator.translate(givenEntries);
		assertEquals(expectedEntries, actualEntries);
	}

	static Stream<Arguments> subsEntriesArgs() {
		return Stream.of(
				argumentSet("Single sentence",
						entries("0 -> 1 To be the man you've got to beat the man."),
						entries("0 -> 1 TO BE THE MAN YOU'VE GOT TO BEAT THE MAN.")),

				argumentSet("Two entries",
						entries("0 -> 1 To be the man you've got to beat the man.",
								"3 -> 4 If you smell..."),
						entries("0 -> 1 TO BE THE MAN YOU'VE GOT TO BEAT THE MAN.",
								"3 -> 4 IF YOU SMELL...")),

				argumentSet("Two-entry sentence",
						entries("0 -> 1 To be the man you've got to",
								"1 -> 2 beat the man. If you smell..."),
						entries("0 -> 1 TO BE THE MAN YOU'VE GOT TO",
								"1 -> 2 BEAT THE MAN. IF YOU SMELL...")),

				argumentSet("Tri-line sentence",
						entries("0 -> 1 To be the man",
								"1 -> 2 you've got to",
								"2 -> 3 beat the man."),
						entries("0 -> 1 TO BE THE MAN YOU'VE",
								"1 -> 2 GOT TO BEAT THE",
								"2 -> 3 MAN.")),

				argumentSet("Two-line with special characters ",
						entries("0 -> 1 To be the man you've got",
								"1 -> 2 to \"beat\" the man. If you smell..."),
						entries("0 -> 1 TO BE THE MAN YOU'VE GOT",
								"1 -> 2 TO \"BEAT\" THE MAN. IF YOU SMELL...")),

				argumentSet("Two-sentence line",
						entries("0 -> 1 To be the man you've got to beat the man. If you smell..."),
						entries("0 -> 1 TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL...")),

				argumentSet("Two-line sentence with long pause",
						entries("0 -> 1 To be the man you've got",
								"5 -> 6 to beat the man. If you smell..."),
						entries("0 -> 1 TO BE THE MAN YOU'VE GOT",
								"5 -> 6 TO BEAT THE MAN. IF YOU SMELL...")),

				argumentSet("Reduce line count when necessary",
						entries("0 -> 1 Which means you are going to waste a whole lot of taxpayer money",
								"1 -> 2 trying to prove there is",
								"2 -> 3 no DNA."),
						entries("0 -> 1 WHICH MEANS YOU ARE GOING TO WASTE A WHOLE LOT OF TAXPAYER MONEY TRYING",
								"1 -> 2 TO PROVE THERE IS NO DNA.")));
	}

	@Test
	void shouldTranslateTwoLineSentenceAsOne() {
		translator.translate(entries(
				"0 -> 1 To be the man you've",
				"1,100 -> 2 got to beat the man."));
		verify(connector).translate(eq("To be the man you've got to beat the man."), eq(EN), eq(BG), any());
	}

	private static class CapitalizingTranslationConnector implements TranslationConnector {

		@Override
		public String translate(String text, Language from, Language to, String context) {
			return text.toUpperCase();
		}
	}
}