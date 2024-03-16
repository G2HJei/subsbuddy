package xyz.zlatanov.subsbuddy.command.translate;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.*;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQuery;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQuery;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQueryHandler;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQuery;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
@AllArgsConstructor
public class TranslateOrchestratorAsync {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private TranslationRepository		translationRepository;
	private ParseLinesQueryHandler		parseLinesQueryHandler;
	private TranslateTextQueryHandler	translateTextQueryHandler;
	private AssembleSubsQueryHandler	assembleSubsQueryHandler;

	@Async
	@Transactional
	protected void orchestrateTranslation(MovieSubtitle sourceSub, Translation translation) {
		try {
			translation.status(IN_PROGRESS);
			val parsedLines = parseLinesQueryHandler.execute(new ParseLinesQuery().subtitleData(sourceSub.subtitleData()));
			val translatedSubsLines = translateTextQueryHandler.execute(new TranslateTextQuery().linesList(parsedLines.lineList()));
			val subsContentFormatted = assembleSubsQueryHandler
					.execute(new AssembleSubsQuery().linesList(translatedSubsLines.linesList()));
			val translatedSub = movieSubtitleRepository.save(new MovieSubtitle()
					.filename(sourceSub.filename())
					.language(BG)
					.subtitleData(subsContentFormatted.content()));
			translation.translatedId(translatedSub.id()).status(COMPLETED);
		} catch (Exception e) {
			translation.status(FAILED);
		}
		translationRepository.save(translation);
	}
}
