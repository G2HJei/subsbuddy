package xyz.zlatanov.subsbuddy.query.availablesubs;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;

import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
public class AvailableSubsQueryHandlerImpl implements AvailableSubsQueryHandler {

	private final boolean					enableOwnerFiltering;
	private final MovieSubtitleRepository	subtitleRepository;
	private final TranslationRepository		translationRepository;

	public AvailableSubsQueryHandlerImpl(@Value("${ENABLE_OWNER_FILTERING:false}") boolean enableOwnerFiltering,
			MovieSubtitleRepository subtitleRepository, TranslationRepository translationRepository) {
		this.enableOwnerFiltering = enableOwnerFiltering;
		this.subtitleRepository = subtitleRepository;
		this.translationRepository = translationRepository;
	}

	@Override
	public AvailableSubsProjection execute(AvailableSubsQuery query) {
		val subtitles = enableOwnerFiltering
				? subtitleRepository.findByOwner(query.owner())
				: subtitleRepository.findByOwnerNotNull();
		return new AvailableSubsProjection(
				subtitles.stream()
						.map(m -> new SubDetails()
								.id(m.id())
								.filename(m.filename())
								.language(m.language())
								.translations(translationRepository.findBySourceId(m.id()).stream()
										.map(t -> new TranslationDetails()
												.id(t.translatedId())
												.language(BG)
												.status(t.status()))
										.toList()))
						.toList());
	}
}