package xyz.zlatanov.subsbuddy.core;

import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.connector.deepl.DeepLTranslationConnector;
import xyz.zlatanov.subsbuddy.core.query.SubtitleEntry;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQuery;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQueryHandler;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQueryHandlerImpl;
import xyz.zlatanov.subsbuddy.core.query.assemblesubs.AssembleSubsQueryProjection;
import xyz.zlatanov.subsbuddy.core.query.parselines.EntryParser;
import xyz.zlatanov.subsbuddy.core.query.parselines.SrtEntryParser;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextProjection;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextQuery;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextQueryHandler;
import xyz.zlatanov.subsbuddy.core.query.translatetext.TranslateTextQueryHandlerImpl;

@RequiredArgsConstructor
public class SubsBuddyClient {

	private final AssembleSubsQueryHandler	assembleSubsHandler;
	private final EntryParser				parseLinesHandler;
	private final TranslationConnector		translationConnector;
	private final TranslateTextQueryHandler	translateTextHandler;

	public static SubsBuddyClient deepL(String... deepLApiKeys) {
		val deepLApiKeysList = Arrays.stream(deepLApiKeys).toList();
		val translationConnector = new DeepLTranslationConnector(deepLApiKeysList);
		return new SubsBuddyClient(
				new AssembleSubsQueryHandlerImpl(),
				new SrtEntryParser(),
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

	private List<SubtitleEntry> extractSourceSubsEntries(String srtSource) {
		return parseLinesHandler.parse(srtSource);
	}

	private TranslateTextProjection translate(List<SubtitleEntry> subtitleEntries) {
		val translateQuery = new TranslateTextQuery().linesList(subtitleEntries);
		return translateTextHandler.execute(translateQuery);
	}

	private AssembleSubsQueryProjection assembleFormattedSubtitles(TranslateTextProjection translatedSubsEntries) {
		val assembleSubsQuery = new AssembleSubsQuery().linesList(translatedSubsEntries.linesList());
		return assembleSubsHandler.execute(assembleSubsQuery);
	}

}
