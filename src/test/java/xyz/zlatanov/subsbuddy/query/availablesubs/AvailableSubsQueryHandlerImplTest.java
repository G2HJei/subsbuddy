package xyz.zlatanov.subsbuddy.query.availablesubs;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
		when(movieSubtitleRepo.findByLanguageOrderByCreatedAtDesc(EN))
				.thenReturn(List.of(
						new MovieSubtitle()
								.id(subtitleId)
								.language(EN)
								.name("name")));
		when(movieSubtitleRepo.findByTranslatedFromSubtitleId(subtitleId))
				.thenReturn(List.of(
						new MovieSubtitle()
								.id(translationId)
								.translatedFromSubtitleId(subtitleId)
								.language(BG)));

		val projection = handler.execute(new AvailableSubsQuery());

		assertEquals(
				new AvailableSubsProjection(List.of(new SubDetails()
						.id(subtitleId)
						.filename("name")
						.language(EN)
						.translations(List.of(new TranslationDetails()
								.id(translationId)
								.language(BG))))),
				projection);

	}

}