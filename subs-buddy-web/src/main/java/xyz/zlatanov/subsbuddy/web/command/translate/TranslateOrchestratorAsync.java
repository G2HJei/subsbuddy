package xyz.zlatanov.subsbuddy.web.command.translate;

import static xyz.zlatanov.subsbuddy.web.domain.TranslationStatus.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.core.client.SubsBuddyClient;
import xyz.zlatanov.subsbuddy.web.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.web.repository.MovieSubtitleRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class TranslateOrchestratorAsync {

	private final MovieSubtitleRepository	movieSubtitleRepository;
	private final SubsBuddyClient			client;

	@Async
	@Transactional
	protected void orchestrateTranslation(MovieSubtitle sourceSub, MovieSubtitle translation) {
		try {
			translation.status(IN_PROGRESS);
			val translatedData = client.translateSrt(sourceSub.subtitleData());
			translation.subtitleData(translatedData)
					.hashCode(translatedData.hashCode());
			translation.status(COMPLETED);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			translation.status(FAILED);
		} finally {
			movieSubtitleRepository.save(translation);
		}
	}
}
