package xyz.zlatanov.subsbuddy.query.translatetext;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLowerCase;
import static java.math.RoundingMode.DOWN;
import static java.math.RoundingMode.HALF_UP;
import static java.time.temporal.ChronoUnit.MILLIS;
import static xyz.zlatanov.subsbuddy.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.domain.Language.EN;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Duration;
import java.util.*;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.val;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import xyz.zlatanov.subsbuddy.connector.translation.TranslationConnector;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@Service
@RequiredArgsConstructor
public class TranslateTextQueryHandlerImpl implements TranslateTextQueryHandler {

	private final Duration				mergeLinesThreshold	= Duration.of(50, MILLIS);

	private final TranslationConnector	translationConnector;

	@Override
	public TranslateTextProjection execute(TranslateTextQuery query) {
		val projection = new TranslateTextProjection();
		val translatedEntries = projection.linesList();
		List<SubtitleEntry> selectionBuffer = new ArrayList<>();
		List<List<SubtitleEntry>> selection = new ArrayList<>(List.of(selectionBuffer));
		for (SubtitleEntry entry : query.linesList()) {
			if (selectionBuffer.isEmpty() || lineContinuation(selectionBuffer, entry)) {
				selectionBuffer.add(entry);
			} else {
				selectionBuffer = new ArrayList<>(List.of(entry));
				selection.add(selectionBuffer);
			}
		}
		selection.forEach(s -> translate(s, translatedEntries));
		return projection;
	}

	private boolean lineContinuation(List<SubtitleEntry> selectionBuffer, SubtitleEntry next) {
		if (selectionBuffer.isEmpty()) {
			return false;
		}
		val last = selectionBuffer.getLast();
		val nextChar = next.text().charAt(0);
		return (Duration.between(last.end(), next.start()).compareTo(mergeLinesThreshold) < 0)
				&& !Arrays.asList('.', '?', "!").contains(last.text().charAt(last.text().length() - 1))
				&& (isLowerCase(nextChar) || isDigit(nextChar));
	}

	private void translate(List<SubtitleEntry> selectionBuffer, List<SubtitleEntry> translatedEntries) {
		val text = String.join(" ", selectionBuffer.stream().map(SubtitleEntry::text).toList());
		val translatedText = translationConnector.translate(text, EN, BG);
		val unescapedTranslatedText = StringEscapeUtils.unescapeHtml4(translatedText);
		translatedEntries.addAll(constructEntries(selectionBuffer, unescapedTranslatedText));
	}

	private List<SubtitleEntry> constructEntries(List<SubtitleEntry> sourceEntries, String translatedText) {
		val entryWeightList = getEntryWeightList(sourceEntries);
		val weightedTextList = toWeightedText(translatedText, entryWeightList);
		List<SubtitleEntry> translatedEntries = new ArrayList<>(sourceEntries.size());
		for (int i = 0; i < sourceEntries.size(); i++) {
			val sourceEntry = sourceEntries.get(i);
			translatedEntries.add(new SubtitleEntry()
					.start(sourceEntry.start())
					.end(sourceEntry.end())
					.text(weightedTextList.get(i)));
		}
		return translatedEntries;
	}

	private List<BigDecimal> getEntryWeightList(List<SubtitleEntry> sourceEntries) {
		val totalLength = sourceEntries.stream()
				.map(SubtitleEntry::text)
				.map(String::length)
				.reduce(0, Integer::sum);
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
		List<Integer> targetLengthList = entryWeightList.stream()
				.map(w -> BigDecimal.valueOf(translatedText.length())
						.multiply(w)
						.divide(BigDecimal.valueOf(100), HALF_UP)
						.intValue())
				.toList();
		final Queue<String> tokens = new LinkedList<>(List.of(WhitespaceTokenizer.INSTANCE.tokenize(translatedText)));
		return targetLengthList.stream()
				.map(targetLength -> {
					val currentLine = new StringBuilder();
					while (!tokens.isEmpty()
							&& currentLine.length() <= targetLength) {
						currentLine.append(tokens.poll()).append(" ");
					}
					return currentLine
							.deleteCharAt(currentLine.length() - 1)
							.toString();
				})
				.toList();
	}
}
