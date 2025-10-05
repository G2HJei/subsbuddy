package xyz.zlatanov.subsbuddy.core.client.typocorrector;

import java.util.List;

import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public interface TypoCorrector {

	SubtitleEntry fix(SubtitleEntry entry);

	default List<SubtitleEntry> fix(List<SubtitleEntry> entries) {
		return entries.stream()
				.map(this::fix)
				.toList();
	}
}
