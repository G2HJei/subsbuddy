package xyz.zlatanov.subsbuddy.query.translatetext.helper;

import static java.math.RoundingMode.*;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import org.apache.commons.text.StringEscapeUtils;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@Slf4j
@RequiredArgsConstructor
public class TranslationHelper {

	private final TranslationConnector	translationConnector;
	private List<SubtitleEntry>			result;

	public List<SubtitleEntry> translate(List<List<SubtitleEntry>> selections) {
		result = new ArrayList<>();
		selections.forEach(this::doTranslate);
		return result;
	}

	private void doTranslate(List<SubtitleEntry> selectionBuffer) {
		val text = String.join(" ", selectionBuffer.stream().map(SubtitleEntry::text).toList());
		log.debug("Translating: {}", text);
		val translatedText = translationConnector.translate(text, EN, BG);
		log.debug("          -> {}", translatedText);
		val unescapedTranslatedText = StringEscapeUtils.unescapeHtml4(translatedText);
		result.addAll(constructEntries(selectionBuffer, unescapedTranslatedText));
	}

	private List<SubtitleEntry> constructEntries(List<SubtitleEntry> sourceEntries, String translatedText) {
		val entryWeightList = getEntryWeightList(sourceEntries);
		val weightedTextList = toWeightedText(translatedText, entryWeightList);
		return createTranslatedEntries(sourceEntries, weightedTextList);
	}

	private List<BigDecimal> getEntryWeightList(List<SubtitleEntry> sourceEntries) {
		val totalLength = sourceEntries.stream()
				.mapToInt(e -> e.text().length())
				.sum();
		return sourceEntries.stream()
				.map(e -> percent(e.text().length(), totalLength))
				.toList();
	}

	public static BigDecimal percent(int base, int total) {
		return BigDecimal.valueOf(base)
				.divide(BigDecimal.valueOf(total), 4, HALF_UP)
				.multiply(BigDecimal.valueOf(100), new MathContext(4, HALF_UP))
				.setScale(0, DOWN);
	}

	private List<String> toWeightedText(String translatedText, List<BigDecimal> entryWeightList) {
		val tokens = new LinkedList<>(List.of(WhitespaceTokenizer.INSTANCE.tokenize(translatedText)));
		val targetLengthList = calculateTargetLengths(translatedText, entryWeightList);
		return buildWeightedTextLines(tokens, targetLengthList);
	}

	private List<Integer> calculateTargetLengths(String translatedText, List<BigDecimal> entryWeightList) {
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

	private List<SubtitleEntry> createTranslatedEntries(List<SubtitleEntry> sourceEntries, List<String> weightedTextList) {
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
