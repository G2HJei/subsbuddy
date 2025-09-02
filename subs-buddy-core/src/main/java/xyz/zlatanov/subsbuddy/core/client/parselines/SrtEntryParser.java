package xyz.zlatanov.subsbuddy.core.client.parselines;

import static xyz.zlatanov.subsbuddy.core.util.ReadUtils.hasEnglishCharacters;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public class SrtEntryParser implements EntryParser {

	public static final String			INFO_LINE		= "-- Translated by 🐻 \"Subs Buddy\" --";
	public static final SubtitleEntry	INFO_ENTRY		= new SubtitleEntry()
			.start(LocalTime.of(0, 0, 0))
			.end(LocalTime.of(0, 0, 10))
			.text(INFO_LINE);
	private static final Pattern		TIMING_PATTERN	= Pattern.compile("([\\d:.,]+) --> ([\\d:.,]+)", Pattern.DOTALL);

	@Override
	public List<SubtitleEntry> parse(String subtitlesData) {
		return parse(subtitlesData, false);
	}

	@Override
	public List<SubtitleEntry> parse(String subtitlesData, boolean addInfoLine) {
		var lineList = getSubtitleEntries(subtitlesData)
				.stream()
				.filter(this::hasValidTimestamps)
				.filter(l -> hasEnglishCharacters(l.text(), 0))
				.map(l -> l.text(trimLine(l.text())))
				.filter(l -> hasText(l.text()))
				.collect(Collectors.toCollection(ArrayList::new));
		if (addInfoLine) {
			addInfoLine(lineList);
		}
		return lineList;
	}

	private List<SubtitleEntry> getSubtitleEntries(String subtitlesData) {
		val entries = new ArrayList<SubtitleEntry>();
		val scanner = new Scanner(subtitlesData);
		SubtitleEntry entryBuffer = null;
		while (scanner.hasNextLine()) {
			val textLine = scanner.nextLine();
			if (!hasText(textLine)) {
				continue;
			}
			val matcher = TIMING_PATTERN.matcher(textLine);
			if (matcher.matches()) {
				flushBuffer(entryBuffer, entries);
				entryBuffer = initBuffer(matcher);
			} else {
				appendText(entryBuffer, textLine);
			}
		}
		flushBuffer(entryBuffer, entries);
		return entries;
	}

	private boolean hasText(String str) {
		return str != null && !str.trim().isEmpty();
	}

	private void flushBuffer(SubtitleEntry currentEntry, ArrayList<SubtitleEntry> entries) {
		if (currentEntry != null) {
			entries.add(currentEntry);
		}
	}

	private SubtitleEntry initBuffer(Matcher matcher) {
		return new SubtitleEntry()
				.start(LocalTime.parse(matcher.group(1).replace(',', '.')))
				.end(LocalTime.parse(matcher.group(2).replace(',', '.')));
	}

	private void appendText(SubtitleEntry entry, String textLine) {
		if (entry != null) {
			entry.text((entry.text() + textLine).trim() + "\n");
		}
	}

	private boolean hasValidTimestamps(SubtitleEntry l) {
		return l.start() != null && l.end() != null;
	}

	private String trimLine(String input) {
		val addSpacesAfterPunctuation = "([.!?])(\\S)";
		val newLines = "\r?\n";
		val doubleWhitespaces = "\\s{2,}";
		val htmlTags = "<[^>]*+>";
		val ambientInSquaredBraces = "\\[[^]]*+]";
		val ambientInAsterisks = "\\*[^\\\\*]*+\\*";
		val trailingDigits = "\\d+$";
		return input
				.replaceAll(addSpacesAfterPunctuation, "$1 $2")
				.replaceAll(newLines, " ")
				.replaceAll(doubleWhitespaces, " ")
				.replaceAll(htmlTags, "")
				.replaceAll(ambientInSquaredBraces, "")
				.replaceAll(ambientInAsterisks, "")
				.trim()
				.replaceAll(trailingDigits, "")
				.trim();
	}

	private void addInfoLine(final List<SubtitleEntry> subEntries) {
		if (noEntriesAtTheStart(subEntries)) {
			subEntries.addFirst(INFO_ENTRY);
		} else {
			glueWithFirst(subEntries);
		}
	}

	private boolean noEntriesAtTheStart(List<SubtitleEntry> lineList) {
		return lineList.isEmpty() || lineList.getFirst().start().isAfter(LocalTime.of(0, 0, 10));
	}

	private void glueWithFirst(List<SubtitleEntry> lineList) {
		val first = lineList.getFirst();
		val infoToGlue = new SubtitleEntry()
				.start(LocalTime.of(0, 0, 0))
				.end(first.start())
				.text(INFO_LINE);
		lineList.addFirst(infoToGlue);
		first.text(INFO_LINE + "\n" + first.text());
	}
}
