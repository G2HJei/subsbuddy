package xyz.zlatanov.subsbuddy.core.client.translatetext;

import java.util.List;

import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public interface EntryTranslator {

	List<SubtitleEntry> translate(List<SubtitleEntry> subtitleEntries);
}
