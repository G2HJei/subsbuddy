package xyz.zlatanov.subsbuddy.core.client;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.client.assemblesubs.SrtSubsAssembler;
import xyz.zlatanov.subsbuddy.core.client.assemblesubs.SubsAssembler;
import xyz.zlatanov.subsbuddy.core.client.parselines.EntryParser;
import xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser;
import xyz.zlatanov.subsbuddy.core.client.translatetext.ContextualEntryTranslator;
import xyz.zlatanov.subsbuddy.core.client.translatetext.EntryTranslator;
import xyz.zlatanov.subsbuddy.core.client.translatetext.log.NopTranslationProgressLogger;
import xyz.zlatanov.subsbuddy.core.client.translatetext.log.TranslationProgressLogger;
import xyz.zlatanov.subsbuddy.core.client.typocorrector.LMTypoCorrector;
import xyz.zlatanov.subsbuddy.core.client.typocorrector.TypoCorrector;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;

public class SubsBuddyClient {

	private final SubsAssembler			assembleSubsHandler;
	private final EntryParser			parseLinesHandler;
	private final TypoCorrector			typoCorrector;
	private final TranslationConnector	translationConnector;
	private final EntryTranslator		translateTextHandler;

	public SubsBuddyClient(TranslationConnector translationConnector) {
		this(translationConnector, new NopTranslationProgressLogger());
	}

	public SubsBuddyClient(TranslationConnector translationConnector, TranslationProgressLogger translationProgressLogger) {
		this.assembleSubsHandler = new SrtSubsAssembler();
		this.parseLinesHandler = new SrtEntryParser();
		this.typoCorrector = new LMTypoCorrector();
		this.translationConnector = translationConnector;
		this.translateTextHandler = new ContextualEntryTranslator(translationConnector, translationProgressLogger);
	}

	public long usagePercent() {
		return translationConnector.usagePercent();
	}

	public String translateSrt(String srtSource) {
		val sourceSubsEntries = parseLinesHandler.parse(srtSource);
		val correctedSubsEntries = typoCorrector.fix(sourceSubsEntries);
		val translatedSubsEntries = translateTextHandler.translate(correctedSubsEntries);
		return assembleSubsHandler.assemble(translatedSubsEntries);
	}
}
