package xyz.zlatanov.subsbuddy.command.translate;

import static xyz.zlatanov.subsbuddy.domain.Language.EN;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.CREATED;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.FAILED;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.exception.TranslationException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
@AllArgsConstructor
public class TranslateFileCommandHandlerImpl implements TranslateFileCommandHandler {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private TranslationRepository		translationRepository;
	private TranslateOrchestratorAsync	asyncOrchestrator; // orchestration logic in separate class to allow @Async

	@Override
	@Transactional
	public void execute(TranslateFileCommand command) {
		val sourceSub = movieSubtitleRepository.findById(command.id()).orElseThrow(TranslationException::new);
		validateSourceSub(sourceSub);
		val existingTranslation = translationRepository.findOneBySourceHashCodeAndStatusNot(sourceSub.subtitleData().hashCode(), FAILED);
		if (existingTranslation == null) {
			initiateTranslation(command, sourceSub);
		} else {
			linkTranslation(sourceSub, existingTranslation);
		}
	}

	private void validateSourceSub(MovieSubtitle sourceSub) {
		if (!translationRepository.findBySourceId(sourceSub.id()).isEmpty()) {
			throw new TranslationException("This subtitle has already been translated.");
		}
		if (sourceSub.language() != EN) {
			throw new TranslationException("Translating non-English subtitles is not supported");
		}
	}

	private void initiateTranslation(TranslateFileCommand command, MovieSubtitle sourceSub) {
		val translation = translationRepository.save(new Translation()
				.sourceId(command.id())
				.sourceHashCode(sourceSub.subtitleData().hashCode())
				.status(CREATED));
		asyncOrchestrator.orchestrateTranslation(sourceSub, translation);
	}

	private void linkTranslation(MovieSubtitle sourceSub, Translation existingTranslation) {
		translationRepository.save(new Translation()
				.sourceId(sourceSub.id())
				.sourceHashCode(sourceSub.subtitleData().hashCode())
				.translatedId(existingTranslation.translatedId()));
	}
}
