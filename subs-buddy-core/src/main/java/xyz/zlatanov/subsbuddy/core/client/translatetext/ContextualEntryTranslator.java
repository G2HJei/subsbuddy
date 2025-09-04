package xyz.zlatanov.subsbuddy.core.client.translatetext;

import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.client.translatetext.support.EntriesGrouper;
import xyz.zlatanov.subsbuddy.core.client.translatetext.support.TranslationDriver;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

@RequiredArgsConstructor
public class ContextualEntryTranslator implements EntryTranslator {

	private final TranslationConnector translationConnector;

	@Override
	public List<SubtitleEntry> translate(List<SubtitleEntry> subtitleEntries) {
		val groupedEntries = new EntriesGrouper(subtitleEntries)
				.group();
		return new TranslationDriver(groupedEntries, translationConnector)
				.translate();
	}

}
