package xyz.zlatanov.subsbuddy.web.command.translate;

import static xyz.zlatanov.subsbuddy.core.domain.Language.BG;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.exception.TranslationException;
import xyz.zlatanov.subsbuddy.web.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.web.domain.TranslationStatus;
import xyz.zlatanov.subsbuddy.web.repository.MovieSubtitleRepository;

@Service
@AllArgsConstructor
public class TranslateSubtitleCommandHandlerImpl implements TranslateFileCommandHandler {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private TranslateOrchestratorAsync	asyncOrchestrator; // orchestration logic is in a separate class to allow @Async

	@Override
	@Transactional
	public void execute(UUID fileId) {
		val sourceSub = getSourceSub(fileId);
		initiateTranslation(sourceSub);
	}

	private MovieSubtitle getSourceSub(UUID subId) {
		return movieSubtitleRepository.findById(subId).orElseThrow(TranslationException::new);
	}

	private void initiateTranslation(MovieSubtitle sourceSub) {
		val translation = movieSubtitleRepository.save(new MovieSubtitle()
				.name(sourceSub.name())
				.language(BG)
				.translatedFromSubtitleId(sourceSub.id())
				.status(TranslationStatus.CREATED));
		asyncOrchestrator.orchestrateTranslation(sourceSub, translation);
	}
}
