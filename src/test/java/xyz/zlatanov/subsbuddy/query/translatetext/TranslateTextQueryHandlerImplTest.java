package xyz.zlatanov.subsbuddy.query.translatetext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.val;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

class TranslateTextQueryHandlerImplTest {

	TranslationConnector		translationConnector	= new CapitalizingTranslationConnector();
	TranslateTextQueryHandler	handler					= new TranslateTextQueryHandlerImpl(translationConnector);

	@ParameterizedTest
	@MethodSource("subsEntriesArgs")
	void shouldTranslate(List<SubtitleEntry> linesToTranslate, List<SubtitleEntry> expectedTranslatedLines) {
		val translation = handler.execute(new TranslateTextQuery().linesList(linesToTranslate));
		assertEquals(expectedTranslatedLines, translation.linesList());
	}

	public static Stream<Arguments> subsEntriesArgs() {
		return Stream.of(
				Arguments.argumentSet("Single sentence",
						entries("0 -> 1 -> To be the man you've got to beat the man."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE GOT TO BEAT THE MAN.")),

				Arguments.argumentSet("Two entries",
						entries("0 -> 1 -> To be the man you've got to beat the man.",
								"3 -> 4 -> If you smell..."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE GOT TO BEAT THE MAN.",
								"3 -> 4 -> IF YOU SMELL...")),

				Arguments.argumentSet("Two-entry sentence",
						entries("0 -> 1 -> To be the man you've got to",
								"1 -> 2 -> beat the man. If you smell..."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE GOT TO",
								"1 -> 2 -> BEAT THE MAN. IF YOU SMELL...")),

				Arguments.argumentSet("Tri-line sentence",
						entries("0 -> 1 -> To be the man",
								"1 -> 2 -> you've got to",
								"2 -> 3 -> beat the man."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE",
								"1 -> 2 -> GOT TO BEAT THE",
								"2 -> 3 -> MAN.")),

				Arguments.argumentSet("Two-line with special characters ",
						entries("0 -> 1 -> To be the man you've got",
								"1 -> 2 -> to \"beat\" the man. If you smell..."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE GOT",
								"1 -> 2 -> TO \"BEAT\" THE MAN. IF YOU SMELL...")),

				Arguments.argumentSet("Two-sentence line",
						entries("0 -> 1 -> To be the man you've got to beat the man. If you smell..."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL...")),

				Arguments.argumentSet("Two-line sentence with long pause",
						entries("0 -> 1 -> To be the man you've got",
								"5 -> 6 -> to beat the man. If you smell..."),
						entries("0 -> 1 -> TO BE THE MAN YOU'VE GOT",
								"5 -> 6 -> TO BEAT THE MAN. IF YOU SMELL...")),

				Arguments.argumentSet("Reduce line count when necessary",
						entries("0 -> 1 -> Which means you are going to waste a whole lot of taxpayer money",
								"1 -> 2 -> trying to prove there is",
								"2 -> 3 -> no DNA."),
						entries("0 -> 1 -> WHICH MEANS YOU ARE GOING TO WASTE A WHOLE LOT OF TAXPAYER MONEY TRYING",
								"1 -> 2 -> TO PROVE THERE IS NO DNA.")));
	}

	private static List<SubtitleEntry> entries(String... entries) {
		return Arrays.stream(entries)
				.map(entry -> entry.split(" -> ", 3))
				.map(parts -> new SubtitleEntry()
						.start(LocalTime.of(0, 0, Integer.parseInt(parts[0].trim())))
						.end(LocalTime.of(0, 0, Integer.parseInt(parts[1].trim())))
						.text(parts[2]))
				.toList();
	}
}