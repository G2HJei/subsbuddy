package xyz.zlatanov.subsbuddy.core.client.parselines;

import java.util.List;

import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public interface EntryParser {

	List<SubtitleEntry> parse(String subtitlesData);

	List<SubtitleEntry> parse(String subtitlesData, boolean addInfoLine);
}
