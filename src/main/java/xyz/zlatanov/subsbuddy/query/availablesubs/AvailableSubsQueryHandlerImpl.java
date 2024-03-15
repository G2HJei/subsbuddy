package xyz.zlatanov.subsbuddy.query.availablesubs;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import xyz.zlatanov.subsbuddy.domain.Language;
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
						.map(m -> {
							Map<Language, String> translationsMap = new LinkedHashMap<>();
							translationRepository.findBySourceId(m.id())
									.forEach(t -> translationsMap.put(BG, t.translatedId())); // currently only BG is supported
							return new SubDetails(m.id(), m.filename(), m.language().toString(), translationsMap);
						})
						.toList());
	}
}