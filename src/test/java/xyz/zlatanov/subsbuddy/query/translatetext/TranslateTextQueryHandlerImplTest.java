package xyz.zlatanov.subsbuddy.query.translatetext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.time.LocalTime;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.val;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

class TranslateTextQueryHandlerImplTest {

	TranslationConnector		translationConnector	= mock(TranslationConnector.class);
	TranslateTextQueryHandler	handler					= new TranslateTextQueryHandlerImpl(translationConnector);

	@BeforeEach
	void setupTranslationConnector() {
		List.of("To be the man you've got to beat the man. If you smell...",
				"To be the man you've got to beat the man.",
				"If you smell...")
				.forEach(t -> when(translationConnector.translate(eq(t), eq(EN), eq(BG)))
						.thenReturn(t.toUpperCase(Locale.ROOT)));
	}

	@Test
	void shouldTranslateSingleSentenceEntry() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("To be the man you've got to beat the man."))));

		assertEquals(1, translation.linesList().size());
		val line = translation.linesList().getFirst();
		assertEquals(LocalTime.of(0, 0, 0), line.start());
		assertEquals(LocalTime.of(0, 0, 1), line.end());
		assertEquals("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN.", line.text());
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

		assertEquals(2, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN."),
				translation.linesList().getFirst());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 3))
						.end(LocalTime.of(0, 0, 4))
						.text("IF YOU SMELL..."),
				translation.linesList().getLast());
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

		assertEquals(2, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO"),
				translation.linesList().getFirst());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("BEAT THE MAN. IF YOU SMELL..."),
				translation.linesList().getLast());
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
		assertEquals(3, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE"),
				translation.linesList().getFirst());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("GOT TO BEAT THE"),
				translation.linesList().get(1));
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 2))
						.end(LocalTime.of(0, 0, 3))
						.text("MAN."),
				translation.linesList().getLast());
	}

	@Test
	void shouldTranslateTwoSentenceLine() {
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to beat the man. If you smell..."))));
		assertEquals(1, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL..."),
				translation.linesList().getFirst());
	}

	@Test
	void ShouldTranslateNoSpacingLine() {
		when(translationConnector.translate(eq("To be the man you've got to beat the man.If you smell..."), eq(EN), eq(BG)))
				.thenReturn("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL...");
		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("To be the man you've got to beat the man.If you smell..."))));
		assertEquals(1, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT TO BEAT THE MAN. IF YOU SMELL..."),
				translation.linesList().getFirst());
	}

	@Test
	void shouldTranslateTwoSentenceLineWithSpecialChars() {
		when(translationConnector
				.translate(eq("To be the man you've got to \"beat\" the man. If you smell..."), eq(EN), eq(BG)))
				.thenReturn("TO BE THE MAN YOU'VE GOT TO \"BEAT\" THE MAN. IF YOU SMELL...");
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

		assertEquals(2, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT"),
				translation.linesList().getFirst());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("TO \"BEAT\" THE MAN. IF YOU SMELL..."),
				translation.linesList().getLast());
	}

	@Test
	void shouldTranslateLongPauseTwoLineSentenceSeparately() {
		when(translationConnector
				.translate(eq("To be the man you've got"), eq(EN), eq(BG)))
				.thenReturn("TO BE THE MAN YOU'VE GOT");
		when(translationConnector
				.translate(eq("to beat the man. If you smell..."), eq(EN), eq(BG)))
				.thenReturn("TO BEAT THE MAN. IF YOU SMELL...");
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

		assertEquals(2, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("TO BE THE MAN YOU'VE GOT"),
				translation.linesList().getFirst());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 5))
						.end(LocalTime.of(0, 0, 6))
						.text("TO BEAT THE MAN. IF YOU SMELL..."),
				translation.linesList().getLast());
	}

	@Test
	void shouldReduceLineCountWhenNecessary() {
		val engText = "Which means you are going to waste a whole lot of taxpayer money trying to prove their side when there is no DNA.";
		val bgText = "Което означава, че ще пропилеете много пари на данъкоплатците, опитвайки се да докажете тяхната страна, когато няма ДНК.";
		when(translationConnector.translate(eq(engText), eq(EN), eq(BG))).thenReturn(bgText);

		val translation = handler.execute(new TranslateTextQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0))
								.end(LocalTime.of(0, 0, 1))
								.text("Which means you are going to waste a whole lot of taxpayer money"),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 1))
								.end(LocalTime.of(0, 0, 2))
								.text("trying to prove their side when there is"),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 2))
								.end(LocalTime.of(0, 0, 3))
								.text("no DNA."))));
		assertEquals(2, translation.linesList().size());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0))
						.end(LocalTime.of(0, 0, 1))
						.text("Което означава, че ще пропилеете много пари на данъкоплатците, опитвайки"),
				translation.linesList().getFirst());
		assertEquals(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 1))
						.end(LocalTime.of(0, 0, 2))
						.text("се да докажете тяхната страна, когато няма ДНК."),
				translation.linesList().getLast());
	}
}