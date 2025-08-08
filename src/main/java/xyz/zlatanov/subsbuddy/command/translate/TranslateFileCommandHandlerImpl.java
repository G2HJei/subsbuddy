package xyz.zlatanov.subsbuddy.command.translate;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.TranslationStatus;
import xyz.zlatanov.subsbuddy.exception.TranslationException;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class TranslateFileCommandHandlerImpl implements TranslateFileCommandHandler {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private TranslateOrchestratorAsync	asyncOrchestrator; // orchestration logic is in a separate class to allow @Async

	@Override
	@Transactional
	public void execute(TranslateFileCommand command) {
		val sourceSub = getSourceSub(command.id());
		initiateTranslation(sourceSub);
	}

	private MovieSubtitle getSourceSub(UUID subId) {
		return movieSubtitleRepository.findById(subId).orElseThrow(TranslationException::new);
	}

	private void initiateTranslation(MovieSubtitle sourceSub) {
		val translation = movieSubtitleRepository.save(new MovieSubtitle()
				.filename(sourceSub.filename())
				.language(BG)
				.translatedFromSubtitleId(sourceSub.id())
				.status(TranslationStatus.CREATED));
		asyncOrchestrator.orchestrateTranslation(sourceSub, translation);
	}
}
