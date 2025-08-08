package xyz.zlatanov.subsbuddy.query.availablesubs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@ExtendWith(MockitoExtension.class)
class AvailableSubsQueryHandlerImplTest {

	@Mock
	MovieSubtitleRepository			movieSubtitleRepo;
	@InjectMocks
	AvailableSubsQueryHandlerImpl	handler;

	UUID							subtitleId		= UUID.randomUUID();
	UUID							translationId	= UUID.randomUUID();

	@Test
	void shouldFindSubs() {
		when(movieSubtitleRepo.findAll())
				.thenReturn(List.of(new MovieSubtitle().id(subtitleId).language(EN).filename("name")));
		when(movieSubtitleRepo.findByTranslatedFromSubtitleId(subtitleId)).thenReturn(List.of(
				new MovieSubtitle()
						.translatedFromSubtitleId(translationId)
						.language(BG)));

		val projection = handler.execute(new AvailableSubsQuery());

		assertEquals(1, projection.result().size());
		val translations = projection.result().getFirst().translations();
		assertEquals(1, translations.size());
		assertTrue(translations.stream().anyMatch(t -> t.language().equals(BG)));
		assertTrue(translations.stream().anyMatch(t -> translationId.equals(t.id())));
	}

}