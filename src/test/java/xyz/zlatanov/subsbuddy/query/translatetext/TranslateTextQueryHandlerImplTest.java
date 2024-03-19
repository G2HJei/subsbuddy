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
import xyz.zlatanov.subsbuddy.connector.translation.google.GoogleTranslateTranslationConnector;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

class TranslateTextQueryHandlerImplTest {

	TranslationConnector		translationConnector	= mock(GoogleTranslateTranslationConnector.class);
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
	void execute_singleSentenceEntry_translates() {
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
	void execute_twoEntries_translates() {
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
	void execute_multiLineSentence_translates() {
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
	void execute_triLineSentence_translates() {
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
	void execute_twoSentenceLine_translates() {
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
	void execute_noSpacingLine_translateWithSpaces() {
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
	void execute_twoSentenceLineWithSpecialChars_translates() {
		when(translationConnector
				.translate(eq("To be the man you've got to \"beat\" the man. If you smell..."), eq(EN), eq( BG)))
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
	void execute_longPauseTwoLineSentence_translatesSeparately() {
		when(translationConnector
				.translate(eq("To be the man you've got"), eq(EN), eq( BG)))
				.thenReturn("TO BE THE MAN YOU'VE GOT");
		when(translationConnector
				.translate(eq("to beat the man. If you smell..."), eq(EN), eq( BG)))
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
}