package xyz.zlatanov.subsbuddy.query.availablesubs;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;

import org.springframework.stereotype.Service;

import lombok.val;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
public class AvailableSubsQueryHandlerImpl implements AvailableSubsQueryHandler {

	private final MovieSubtitleRepository	subtitleRepository;
	private final TranslationRepository		translationRepository;

	public AvailableSubsQueryHandlerImpl(MovieSubtitleRepository subtitleRepository, TranslationRepository translationRepository) {
		this.subtitleRepository = subtitleRepository;
		this.translationRepository = translationRepository;
	}

	@Override
	public AvailableSubsProjection execute(AvailableSubsQuery query) {
		val subtitles = subtitleRepository.findAll();
		return new AvailableSubsProjection(
				subtitles.stream()
						.map(m -> new SubDetails()
								.id(m.id())
								.filename(m.filename())
								.language(m.language())
								.translations(translationRepository.findBySourceId(m.id()).stream()
										.map(t -> new TranslationDetails()
												.id(t.translationId())
												.language(BG)
												.status(t.status()))
										.toList()))
						.toList());
	}
}