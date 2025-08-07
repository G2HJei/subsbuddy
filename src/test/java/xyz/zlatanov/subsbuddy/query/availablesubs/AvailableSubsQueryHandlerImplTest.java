package xyz.zlatanov.subsbuddy.query.availablesubs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

class AvailableSubsQueryHandlerImplTest {

	MovieSubtitleRepository		movieSubtitleRepo	= mock(MovieSubtitleRepository.class);
	TranslationRepository		translationRepo		= mock(TranslationRepository.class);
	AvailableSubsQueryHandler	handler				= new AvailableSubsQueryHandlerImpl(movieSubtitleRepo, translationRepo);

	@BeforeEach
	void setup() {
		when(movieSubtitleRepo.findAll())
				.thenReturn(List.of(new MovieSubtitle().id("subtitleId").language(EN).filename("name")));
		when(translationRepo.findBySourceId("subtitleId")).thenReturn(List.of(
				new Translation()
						.id("translationId")
						.sourceId("id")
						.translationId("translatedId")));
	}

	@Test
	void shouldFindSubs() {
		val projection = handler.execute(new AvailableSubsQuery());
		assertEquals(1, projection.result().size());
		val translations = projection.result().getFirst().translations();
		assertEquals(1, translations.size());
		assertTrue(translations.stream().anyMatch(t -> t.language().equals(BG)));
		assertTrue(translations.stream().anyMatch(t -> "translatedId".equals(t.id())));
	}

}