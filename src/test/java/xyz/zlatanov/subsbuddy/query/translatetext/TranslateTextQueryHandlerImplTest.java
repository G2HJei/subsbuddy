package xyz.zlatanov.subsbuddy.query.translatetext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.val;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

class TranslateTextQueryHandlerImplTest {

	TranslationConnector		translationConnector	= new CapitalizingTranslationConnector();
	TranslateTextQueryHandler	handler					= new TranslateTextQueryHandlerImpl(translationConnector);

	@Test
	void shouldTranslateSingleSentenceEntry() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("To be the man you've got to beat the man."))));

		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN.")),
				translation.linesList());
	}

	@Test
	void shouldTranslateTwoEntries() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to beat the man."),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 3))
								.end(LocalTime.of(0, 0, 4))
								.text("If you smell..."))));

		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN."),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 3))
						.end(LocalTime.of(0, 0, 4))
						.text("IF YOU SMELL...")),
				translation.linesList());
	}

	@Test
	void shouldTranslateMultipleSentences() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to"),

						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 1))
								.end(LocalTime.of(0, 0, 2))
								.text("beat the man. If you smell..."))));

		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO"),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("BEAT THE MAN. IF YOU SMELL...")),
				translation.linesList());
	}

	@Test
	void shouldTranslateTriLineSentence() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
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
								.text("beat the man."))));
		assertEquals(List.of(
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
						.text("MAN.")),
				translation.linesList());
	}

	@Test
	void shouldTranslateTwoSentenceLine() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to beat the man. If you smell..."))));
		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL...")),
				translation.linesList());
	}

	@Test
	void shouldTranslateTwoSentenceLineWithSpecialChars() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got"),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 1))
								.end(LocalTime.of(0, 0, 2))
								.text("to \"beat\" the man. If you smell..."))));
		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT"),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("TO \"BEAT\" THE MAN. IF YOU SMELL...")),
				translation.linesList());
	}

	@Test
	void shouldTranslateLongPauseTwoLineSentenceSeparately() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got"),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 5))
								.end(LocalTime.of(0, 0, 6))
								.text("to beat the man. If you smell..."))));

		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT"),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 5))
						.end(LocalTime.of(0, 0, 6))
						.text("TO BEAT THE MAN. IF YOU SMELL...")),
				translation.linesList());
	}

	@Test
	void shouldReduceLineCountWhenNecessary() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
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
								.text("no DNA."))));

		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("WHICH MEANS YOU ARE GOING TO WASTE A WHOLE LOT OF TAXPAYER MONEY TRYING"),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("TO PROVE THERE IS NO DNA.")),
				translation.linesList());
	}
}