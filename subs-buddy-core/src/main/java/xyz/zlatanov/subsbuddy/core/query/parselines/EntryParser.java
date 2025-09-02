package xyz.zlatanov.subsbuddy.core.query.parselines;

import java.util.List;

import xyz.zlatanov.subsbuddy.core.query.SubtitleEntry;

public interface EntryParser {

	List<SubtitleEntry> parse(String subtitlesData);

	List<SubtitleEntry> parse(String subtitlesData, boolean addInfoLine);
}
