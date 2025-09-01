package xyz.zlatanov.subsbuddy.core.query.translatetext.helper;

import static java.math.RoundingMode.*;
import static lombok.AccessLevel.PRIVATE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import lombok.NoArgsConstructor;
import lombok.val;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import xyz.zlatanov.subsbuddy.core.query.SubtitleEntry;

@NoArgsConstructor(access = PRIVATE)
public class EntriesConstructor {

	private List<SubtitleEntry>	sourceEntries;
	private String				translatedText;
	private List<BigDecimal>	entryWeightList;
	private List<String>		weightedTextList;

	public static EntriesConstructor create(List<SubtitleEntry> sourceEntries, String translatedText) {
		val constructor = new EntriesConstructor();
		constructor.sourceEntries = sourceEntries;
		constructor.translatedText = translatedText;
		constructor.calcEntryWeightList();
		constructor.calcWeightedText();
		return constructor;
	}

	public List<SubtitleEntry> construct() {
		return createTranslatedEntries();
	}

	private void calcEntryWeightList() {
		val totalLength = sourceEntries.stream()
				.mapToInt(e -> e.text().length())
				.sum();
		entryWeightList = sourceEntries.stream()
				.map(e -> percent(e.text().length(), totalLength))
				.toList();
	}

	private static BigDecimal percent(int base, int total) {
		return BigDecimal.valueOf(base)
				.divide(BigDecimal.valueOf(total), 4, HALF_UP)
				.multiply(BigDecimal.valueOf(100), new MathContext(4, HALF_UP))
				.setScale(0, DOWN);
	}

	private void calcWeightedText() {
		val tokens = new LinkedList<>(List.of(WhitespaceTokenizer.INSTANCE.tokenize(translatedText)));
		val targetLengthList = calculateTargetLengths();
		weightedTextList = buildWeightedTextLines(tokens, targetLengthList);
	}

	private List<Integer> calculateTargetLengths() {
		return entryWeightList.stream()
				.map(w -> BigDecimal.valueOf(translatedText.length())
						.multiply(w)
						.divide(BigDecimal.valueOf(100), HALF_DOWN)
						.intValue())
				.toList();
	}

	private List<String> buildWeightedTextLines(Queue<String> tokens, List<Integer> targetLengthList) {
		return targetLengthList.stream()
				.map(targetLength -> buildSingleLine(tokens, targetLength))
				.filter(Objects::nonNull)
				.toList();
	}

	private String buildSingleLine(Queue<String> tokens, int targetLength) {
		val currentLine = new StringBuilder();
		while (!tokens.isEmpty() && currentLine.length() <= targetLength) {
			currentLine.append(tokens.poll()).append(" ");
		}
		return currentLine.isEmpty() ? null
				: currentLine.deleteCharAt(currentLine.length() - 1).toString();
	}

	private List<SubtitleEntry> createTranslatedEntries() {
		val translatedEntries = new ArrayList<SubtitleEntry>(sourceEntries.size());
		for (int i = 0; i < sourceEntries.size() && i < weightedTextList.size(); i++) {
			translatedEntries.add(createSingleEntry(sourceEntries.get(i), weightedTextList.get(i)));
		}
		return translatedEntries;
	}

	private SubtitleEntry createSingleEntry(SubtitleEntry sourceEntry, String translatedText) {
		return new SubtitleEntry()
				.start(sourceEntry.start())
				.end(sourceEntry.end())
				.text(translatedText);
	}
}
