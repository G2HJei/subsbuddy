package xyz.zlatanov.subsbuddy.command.translate;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.*;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQuery;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.assemblesubs.AssembleSubsQueryProjection;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesProjection;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQuery;
import xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQueryHandler;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextProjection;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQuery;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;

@Service
@Slf4j
@AllArgsConstructor
public class TranslateOrchestratorAsync {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private ParseLinesQueryHandler		parseLinesQueryHandler;
	private TranslateTextQueryHandler	translateTextQueryHandler;
	private AssembleSubsQueryHandler	assembleSubsQueryHandler;

	@Async
	@Transactional
	protected void orchestrateTranslation(MovieSubtitle sourceSub, Translation translation) {
		try {
			doTranslation(sourceSub, translation);
		} catch (Exception e) {
			log.error(ExceptionUtils.getStackTrace(e));
			translation.status(FAILED);
		}
	}

	private void doTranslation(MovieSubtitle sourceSub, Translation translation) {
		translation.status(IN_PROGRESS);
		val sourceSubsEntries = extractSourceSubsEntries(sourceSub);
		val translatedSubsEntries = translate(sourceSubsEntries);
		val subsContentFormatted = assembleFormattedSubtitles(translatedSubsEntries);
		saveTranslatedSubtitles(sourceSub, translation, subsContentFormatted);
	}

	private ParseLinesProjection extractSourceSubsEntries(MovieSubtitle sourceSub) {
		val parseLinesQuery = new ParseLinesQuery()
				.addSubsBuddyInfo(true)
				.subtitleData(sourceSub.subtitleData());
		return parseLinesQueryHandler.execute(parseLinesQuery);
	}

	private TranslateTextProjection translate(ParseLinesProjection sourceSubsEntries) {
		val translateQuery = new TranslateTextQuery().linesList(sourceSubsEntries.lineList());
		return translateTextQueryHandler.execute(translateQuery);
	}

	private AssembleSubsQueryProjection assembleFormattedSubtitles(TranslateTextProjection translatedSubsEntries) {
		val assembleSubsQuery = new AssembleSubsQuery().linesList(translatedSubsEntries.linesList());
		return assembleSubsQueryHandler.execute(assembleSubsQuery);
	}

	private void saveTranslatedSubtitles(MovieSubtitle sourceSub, Translation translation,
			AssembleSubsQueryProjection subsContentFormatted) {
		val translatedSub = movieSubtitleRepository.save(new MovieSubtitle()
				.filename(sourceSub.filename())
				.language(BG)
				.subtitleData(subsContentFormatted.content()));
		translation.translatedSubtitleId(translatedSub.id()).status(COMPLETED);
	}
}
