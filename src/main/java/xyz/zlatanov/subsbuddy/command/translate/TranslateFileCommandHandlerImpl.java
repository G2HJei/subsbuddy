package xyz.zlatanov.subsbuddy.command.translate;

import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.domain.MovieSubtitle;
import xyz.zlatanov.subsbuddy.domain.Translation;
import xyz.zlatanov.subsbuddy.exception.TranslationException;
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
public class TranslateFileCommandHandlerImpl implements TranslateFileCommandHandler {

	private MovieSubtitleRepository			movieSubtitleRepository;
	private TranslationRepository			translationRepository;
	private ParseLinesQueryHandler			parseLinesQueryHandler;
	private TranslateTextQueryHandler		translateTextQueryHandler;
	private AssembleSubsQueryHandler assembleSubsQueryHandler;

	@Async
	@Override
	@Transactional
	public void execute(TranslateFileCommand command) {
		val sourceSub = movieSubtitleRepository.findById(command.id()).orElseThrow(TranslationException::new);
		validateSourceSub(sourceSub);
		val matchedByHashCode = movieSubtitleRepository.findBySourceHashCode(sourceSub.subtitleData().hashCode());
		if (matchedByHashCode == null) {
			orchestrateTranslation(sourceSub);
		} else {
			linkTranslation(sourceSub, matchedByHashCode);
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

	private void orchestrateTranslation(MovieSubtitle sourceSub) {
		val parsedLines = parseLinesQueryHandler.execute(new ParseLinesQuery().subtitleData(sourceSub.subtitleData()));
		val translatedSubsLines = translateTextQueryHandler.execute(new TranslateTextQuery().linesList(parsedLines.lineList()));
		val subsContentFormatted = assembleSubsQueryHandler
				.execute(new AssembleSubsQuery().linesList(translatedSubsLines.linesList()));
		val translatedSub = movieSubtitleRepository.save(new MovieSubtitle()
				.filename(sourceSub.filename())
				.language(BG)
				.subtitleData(subsContentFormatted.content())
				.sourceHashCode(sourceSub.hashCode()));
		translationRepository.save(new Translation().sourceId(sourceSub.id()).translatedId(translatedSub.id()));
	}

	private void linkTranslation(MovieSubtitle sourceSub, MovieSubtitle matchedByHashCode) {
		if (translationRepository.findBySourceIdAndTranslatedId(sourceSub.id(), matchedByHashCode.id()) == null) {
			translationRepository.save(new Translation().sourceId(sourceSub.id()).translatedId(matchedByHashCode.id()));
		}
	}
}
