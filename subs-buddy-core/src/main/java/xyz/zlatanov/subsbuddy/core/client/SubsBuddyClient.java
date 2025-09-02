package xyz.zlatanov.subsbuddy.core.client;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.client.assemblesubs.SrtSubsAssembler;
import xyz.zlatanov.subsbuddy.core.client.assemblesubs.SubsAssembler;
import xyz.zlatanov.subsbuddy.core.client.parselines.EntryParser;
import xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser;
import xyz.zlatanov.subsbuddy.core.client.translatetext.ContextualEntryTranslator;
import xyz.zlatanov.subsbuddy.core.client.translatetext.EntryTranslator;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.connector.deepl.DeepLTranslationConnector;

@RequiredArgsConstructor
public class SubsBuddyClient {

	private final SubsAssembler			assembleSubsHandler;
	private final EntryParser			parseLinesHandler;
	private final TranslationConnector	translationConnector;
	private final EntryTranslator		translateTextHandler;

	public static SubsBuddyClient deepL(String... deepLApiKeys) {
		val deepLApiKeysList = Arrays.stream(deepLApiKeys).toList();
		val translationConnector = new DeepLTranslationConnector(deepLApiKeysList);
		return new SubsBuddyClient(
				new SrtSubsAssembler(),
				new SrtEntryParser(),
				translationConnector,
				new ContextualEntryTranslator(translationConnector));
	}

	public long usagePercent() {
		return translationConnector.usagePercent();
	}

	public String translateSrt(String srtSource) {
		val sourceSubsEntries = parseLinesHandler.parse(srtSource);
		val translatedSubsEntries = translateTextHandler.translate(sourceSubsEntries);
		return assembleSubsHandler.assemble(translatedSubsEntries);
	}
}
