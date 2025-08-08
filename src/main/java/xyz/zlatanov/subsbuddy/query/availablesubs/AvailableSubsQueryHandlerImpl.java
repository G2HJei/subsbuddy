package xyz.zlatanov.subsbuddy.query.availablesubs;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@RequiredArgsConstructor
public class AvailableSubsQueryHandlerImpl implements AvailableSubsQueryHandler {

	private final MovieSubtitleRepository subtitleRepository;

	@Override
	public AvailableSubsProjection execute(AvailableSubsQuery query) {
		val subtitles = subtitleRepository.findAll();
		return new AvailableSubsProjection(
				subtitles.stream()
						.map(m -> new SubDetails()
								.id(m.id())
								.filename(m.filename())
								.language(m.language())
								.translations(subtitleRepository.findByTranslatedFromSubtitleId(m.id()).stream()
										.map(t -> new TranslationDetails()
												.id(t.translatedFromSubtitleId())
												.language(t.language())
												.status(t.status()))
										.toList()))
						.toList());
	}
}