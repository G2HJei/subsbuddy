package xyz.zlatanov.subsbuddy.core.client;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.client.assemblesubs.SrtSubsAssembler;
import xyz.zlatanov.subsbuddy.core.client.assemblesubs.SubsAssembler;
import xyz.zlatanov.subsbuddy.core.client.parselines.EntryParser;
import xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser;
import xyz.zlatanov.subsbuddy.core.client.translatetext.ContextualEntryTranslator;
import xyz.zlatanov.subsbuddy.core.client.translatetext.EntryTranslator;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;

public class SubsBuddyClient {

	private final SubsAssembler			assembleSubsHandler;
	private final EntryParser			parseLinesHandler;
	private final TranslationConnector	translationConnector;
	private final EntryTranslator		translateTextHandler;

	public SubsBuddyClient(TranslationConnector translationConnector) {
		this.assembleSubsHandler = new SrtSubsAssembler();
		this.parseLinesHandler = new SrtEntryParser();
		this.translationConnector = translationConnector;
		this.translateTextHandler = new ContextualEntryTranslator(translationConnector);
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
