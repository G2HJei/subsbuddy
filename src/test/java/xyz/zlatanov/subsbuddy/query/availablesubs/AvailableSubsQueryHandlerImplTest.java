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
	void setup(){
		when(movieSubtitleRepo.findByOwner("owner1")).thenReturn(List.of(new MovieSubtitle().id("subtitleId").language(EN).filename("name")));
		when(movieSubtitleRepo.findByOwner("owner2")).thenReturn(List.of());
		when(movieSubtitleRepo.findByOwner(null)).thenReturn(List.of());
		when(translationRepo.findBySourceId("subtitleId")).thenReturn(List.of(
				new Translation()
					.id("translationId")
					.sourceId("id")
					.translatedId("translatedId")));
	}

	@Test
	void execute_variableOwners_returnsSelectedOwnerWithTranslation() {
		val projection = handler.execute(new AvailableSubsQuery().owner("owner1"));
		assertEquals(1, projection.result().size());
		val translations = projection.result().getFirst().translations();
		assertEquals(1, translations.size());
		assertTrue(translations.containsKey(BG));
		assertEquals("translatedId", translations.get(BG));
	}

	@Test
	void execute_noSelectedOwner_returnsEmpty() {
		assertTrue(handler.execute(new AvailableSubsQuery().owner(null)).result().isEmpty());

	}

	@Test
	void execute_otherOwners_returnsEmpty() {
		assertTrue(handler.execute(new AvailableSubsQuery().owner("owner2")).result().isEmpty());
	}

}