package xyz.zlatanov.subsbuddy.query.translatetext.helper;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.time.temporal.ChronoUnit.MILLIS;
import static lombok.AccessLevel.PRIVATE;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@RequiredArgsConstructor(access = PRIVATE)
public class EntriesGrouper {

	public static final Duration		MERGE_LINES_THRESHOLD	= Duration.of(100, MILLIS);

	private final List<SubtitleEntry>	entries;

	public static EntriesGrouper create(List<SubtitleEntry> entries) {
		return new EntriesGrouper(entries);
	}

	public List<List<SubtitleEntry>> group() {
		var selectionBuffer = new ArrayList<SubtitleEntry>();
		val selection = new ArrayList<List<SubtitleEntry>>(List.of(selectionBuffer));
		for (val entry : entries) {
			selectionBuffer = processEntry(entry, selectionBuffer, selection);
		}
		return selection;
	}

	private ArrayList<SubtitleEntry> processEntry(SubtitleEntry entry, ArrayList<SubtitleEntry> selectionBuffer,
			ArrayList<List<SubtitleEntry>> selection) {
		if (selectionBuffer.isEmpty() || lineContinuation(selectionBuffer, entry)) {
			selectionBuffer.add(entry);
			return selectionBuffer;
		}
		val newBuffer = new ArrayList<>(List.of(entry));
		selection.add(newBuffer);
		return newBuffer;
	}

	private boolean lineContinuation(List<SubtitleEntry> selectionBuffer, SubtitleEntry next) {
		if (selectionBuffer.isEmpty()) {
			return false;
		}
		val last = selectionBuffer.getLast();
		val nextChar = next.text().charAt(0);
		val lastChar = last.text().charAt(last.text().length() - 1);
		return Duration.between(last.end(), next.start()).compareTo(MERGE_LINES_THRESHOLD) <= 0
				&& !Arrays.asList('.', '?', '!').contains(lastChar)
				&& (isLowerCase(nextChar) || isDigit(nextChar));
	}
}
