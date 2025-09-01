package xyz.zlatanov.subsbuddy.core;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.connector.deepl.DeepLTranslationConnector;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQuery;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQueryHandler;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQueryHandlerImpl;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQueryProjection;
import xyz.zlatanov.subsbuddy.core.query.parselines.ParseLinesProjection;
import xyz.zlatanov.subsbuddy.core.query.parselines.ParseLinesQuery;
import xyz.zlatanov.subsbuddy.core.query.parselines.ParseLinesQueryHandler;
import xyz.zlatanov.subsbuddy.core.query.parselines.ParseLinesQueryHandlerImpl;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextProjection;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextQuery;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextQueryHandlerImpl;

@RequiredArgsConstructor
public class SubsBuddyClient {

	private final AssembleSubsQueryHandler	assembleSubsHandler;
	private final ParseLinesQueryHandler	parseLinesHandler;
	private final TranslationConnector		translationConnector;
	private final TranslateTextQueryHandler	translateTextHandler;

	public static SubsBuddyClient deepL(String... deepLApiKeys) {
		val deepLApiKeysList = Arrays.stream(deepLApiKeys).toList();
		val translationConnector = new DeepLTranslationConnector(deepLApiKeysList);
		return new SubsBuddyClient(
				new AssembleSubsQueryHandlerImpl(),
				new ParseLinesQueryHandlerImpl(),
				translationConnector,
				new TranslateTextQueryHandlerImpl(translationConnector));
	}

	public long usagePercent() {
		return translationConnector.usagePercent();
	}

	public String translateSrt(String srtSource) {
		val sourceSubsEntries = extractSourceSubsEntries(srtSource);
		val translatedSubsEntries = translate(sourceSubsEntries);
		val subsContentFormatted = assembleFormattedSubtitles(translatedSubsEntries);
		return subsContentFormatted.content();
	}

	private ParseLinesProjection extractSourceSubsEntries(String srtSource) {
		val parseLinesQuery = new ParseLinesQuery()
				.addSubsBuddyInfo(true)
				.subtitleData(srtSource);
		return parseLinesHandler.execute(parseLinesQuery);
	}

	private TranslateTextProjection translate(ParseLinesProjection sourceSubsEntries) {
		val translateQuery = new TranslateTextQuery().linesList(sourceSubsEntries.lineList());
		return translateTextHandler.execute(translateQuery);
	}

	private AssembleSubsQueryProjection assembleFormattedSubtitles(TranslateTextProjection translatedSubsEntries) {
		val assembleSubsQuery = new AssembleSubsQuery().linesList(translatedSubsEntries.linesList());
		return assembleSubsHandler.execute(assembleSubsQuery);
	}

}
