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
	AvailableSubsQueryHandler	handler				= new AvailableSubsQueryHandlerImpl(true, movieSubtitleRepo, translationRepo);

	@BeforeEach
	void setup() {
		when(movieSubtitleRepo.findByOwner("owner1"))
				.thenReturn(List.of(new MovieSubtitle().id("subtitleId").language(EN).filename("name")));
		when(movieSubtitleRepo.findByOwner("owner2")).thenReturn(List.of());
		when(movieSubtitleRepo.findByOwner(null)).thenReturn(List.of());
		when(translationRepo.findBySourceId("subtitleId")).thenReturn(List.of(
				new Translation()
						.id("translationId")
						.sourceId("id")
						.translatedId("translatedId")));
		when(movieSubtitleRepo.findAll())
				.thenReturn(List.of(new MovieSubtitle(), new MovieSubtitle(), new MovieSubtitle()));
	}

	@Test
	void shouldFilterOwners() {
		val projection = handler.execute(new AvailableSubsQuery().owner("owner1"));
		assertEquals(1, projection.result().size());
		val translations = projection.result().getFirst().translations();
		assertEquals(1, translations.size());
		assertTrue(translations.stream().anyMatch(t -> t.language().equals(BG)));
		assertTrue(translations.stream().anyMatch(t -> "translatedId".equals(t.id())));
	}

	@Test
	void shouldFilterOwners2() {
		assertTrue(handler.execute(new AvailableSubsQuery().owner("owner2")).result().isEmpty());
	}

	@Test
	void shouldFilterOwnersWhenNoneDefined() {
		assertTrue(handler.execute(new AvailableSubsQuery().owner(null)).result().isEmpty());

	}

	@Test
	void shouldIgnoreOwnerFiltering() {
		val handler = new AvailableSubsQueryHandlerImpl(false, movieSubtitleRepo, translationRepo);
		val projection = handler.execute(new AvailableSubsQuery().owner("it-doesnt-matter"));
		assertEquals(3, projection.result().size());
	}

}