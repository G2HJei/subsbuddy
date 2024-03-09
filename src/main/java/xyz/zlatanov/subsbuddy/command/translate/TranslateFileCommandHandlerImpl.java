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
import xyz.zlatanov.subsbuddy.query.formatsubscontent.FormatSubsContentQuery;
import xyz.zlatanov.subsbuddy.query.formatsubscontent.FormatSubsQueryHandler;
import xyz.zlatanov.subsbuddy.query.formatsubscontent.SubsLine;
import xyz.zlatanov.subsbuddy.query.splitlines.SplitLinesQuery;
import xyz.zlatanov.subsbuddy.query.splitlines.SplitLinesQueryHandler;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQuery;
import xyz.zlatanov.subsbuddy.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.repository.MovieSubtitleRepository;
import xyz.zlatanov.subsbuddy.repository.TranslationRepository;

@Service
@AllArgsConstructor
public class TranslateFileCommandHandlerImpl implements TranslateFileCommandHandler {

	private MovieSubtitleRepository		movieSubtitleRepository;
	private TranslationRepository		translationRepository;
	private SplitLinesQueryHandler		splitLinesQueryHandler;
	private TranslateTextQueryHandler	translateTextQueryHandler;
	private FormatSubsQueryHandler		formatSubsQueryHandler;

	@Async
	@Override
	@Transactional
	public void execute(TranslateFileCommand command) {
		val sub = movieSubtitleRepository.findById(command.id()).orElseThrow(TranslationException::new);
		if (!translationRepository.findBySourceId(sub.id()).isEmpty()) {
			throw new TranslationException("This subtitle has already been translated.");
		}
		if (sub.language() != EN) {
			throw new TranslationException("Translation non-English subtitles is not supported");
		}
		val splitLinesProjection = splitLinesQueryHandler.execute(new SplitLinesQuery().subtitleData(sub.subtitleData()));
		val translatedSubsLines = splitLinesProjection.lineList().stream()
				.map(l -> new SubsLine()
						.start(l.start())
						.end(l.end())
						.text(translateTextQueryHandler.execute(new TranslateTextQuery().text(l.text())).text()))
				.toList();
		val subsContentFormatted = formatSubsQueryHandler.execute(new FormatSubsContentQuery().linesList(translatedSubsLines));
		val subBg = movieSubtitleRepository.save(new MovieSubtitle()
				.filename(sub.filename())
				.language(BG)
				.subtitleData(subsContentFormatted.content()));
		translationRepository.save(new Translation().sourceId(sub.id()).translationId(subBg.id()));
	}
}
