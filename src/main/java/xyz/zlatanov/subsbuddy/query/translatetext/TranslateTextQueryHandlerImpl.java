package xyz.zlatanov.subsbuddy.query.translatetext;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;
import xyz.zlatanov.subsbuddy.query.translatetext.helper.TranslationHelper;

@Slf4j
@Service
@RequiredArgsConstructor
public class TranslateTextQueryHandlerImpl implements TranslateTextQueryHandler {

	public static final Duration		MERGE_LINES_THRESHOLD	= Duration.of(100, MILLIS);

	private final TranslationConnector	translationConnector;

	@Override
	public TranslateTextProjection execute(TranslateTextQuery query) {
		val selection = groupEntriesIntoSelections(query.linesList());
		val translatedLines = new TranslationHelper(translationConnector).translate(selection);
		return new TranslateTextProjection()
				.linesList(translatedLines);
	}

	private List<List<SubtitleEntry>> groupEntriesIntoSelections(List<SubtitleEntry> entries) {
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
