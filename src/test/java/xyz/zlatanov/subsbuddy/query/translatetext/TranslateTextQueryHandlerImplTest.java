package xyz.zlatanov.subsbuddy.query.translatetext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
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
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to beat the man.")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN."))),

				Arguments.argumentSet("Two entries",
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to beat the man."),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 3))
										.end(LocalTime.of(0, 0, 4))
										.text("If you smell...")),
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN."),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 3))
										.end(LocalTime.of(0, 0, 4))
										.text("IF YOU SMELL..."))),

				Arguments.argumentSet("Two-entry sentence",
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("To be the man you've got to"),

								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("beat the man. If you smell...")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("TO BE THE MAN YOU'VE GOT TO"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("BEAT THE MAN. IF YOU SMELL..."))),

				Arguments.argumentSet("Tri-line sentence",
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("To be the man"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("you've got to"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 2))
										.end(LocalTime.of(0, 0, 3))
										.text("beat the man.")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("TO BE THE MAN YOU'VE"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("GOT TO BEAT THE"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 2))
										.end(LocalTime.of(0, 0, 3))
										.text("MAN."))),

				Arguments.argumentSet("Two-line with special characters ",
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("To be the man you've got"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("to \"beat\" the man. If you smell...")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("TO BE THE MAN YOU'VE GOT"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("TO \"BEAT\" THE MAN. IF YOU SMELL..."))),

				Arguments.argumentSet("Two-sentence line",
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("To be the man you've got to beat the man. If you smell...")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL..."))),

				Arguments.argumentSet("Two-line sentence with long pause",
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("To be the man you've got"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 5))
										.end(LocalTime.of(0, 0, 6))
										.text("to beat the man. If you smell...")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("TO BE THE MAN YOU'VE GOT"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 5))
										.end(LocalTime.of(0, 0, 6))
										.text("TO BEAT THE MAN. IF YOU SMELL..."))),

				Arguments.argumentSet("Reduce line count when necessary",
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("Which means you are going to waste a whole lot of taxpayer money"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("trying to prove there is"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 2))
										.end(LocalTime.of(0, 0, 3))
										.text("no DNA.")),
						List.of(
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 0))
										.end(LocalTime.of(0, 0, 1))
										.text("WHICH MEANS YOU ARE GOING TO WASTE A WHOLE LOT OF TAXPAYER MONEY TRYING"),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 1))
										.end(LocalTime.of(0, 0, 2))
										.text("TO PROVE THERE IS NO DNA."))));
	}
}