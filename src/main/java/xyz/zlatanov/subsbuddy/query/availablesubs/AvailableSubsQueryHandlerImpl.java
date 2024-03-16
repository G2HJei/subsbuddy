package xyz.zlatanov.subsbuddy.query.availablesubs;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
@AllArgsConstructor
public class AvailableSubsQueryHandlerImpl implements AvailableSubsQueryHandler {

	private MovieSubtitleRepository	subtitleRepository;
	private TranslationRepository	translationRepository;

	@Override
	public AvailableSubsProjection execute(AvailableSubsQuery query) {
		return new AvailableSubsProjection(
				subtitleRepository.findByOwner(query.owner()).stream()
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