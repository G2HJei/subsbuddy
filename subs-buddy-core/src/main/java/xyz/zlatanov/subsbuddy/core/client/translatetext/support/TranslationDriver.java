package xyz.zlatanov.subsbuddy.core.client.translatetext.support;

import static xyz.zlatanov.subsbuddy.core.domain.Language.BG;
import static xyz.zlatanov.subsbuddy.core.domain.Language.EN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.text.StringEscapeUtils;

import lombok.RequiredArgsConstructor;
import lombok.val;
import xyz.zlatanov.subsbuddy.core.connector.TranslationConnector;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

@RequiredArgsConstructor
public class TranslationDriver {

	private final List<List<SubtitleEntry>>	selections;
	private final TranslationConnector		translationConnector;
	private final List<SubtitleEntry>		result	= new ArrayList<>();

	public List<SubtitleEntry> translate() {
		if (result.isEmpty()) {
			selections.forEach(this::doTranslate);
		}
		return result;
	}

	private void doTranslate(List<SubtitleEntry> selectionBuffer) {
		val text = connectTranslation(selectionBuffer);
		val translatedEntries = EntriesConstructor
				.create(selectionBuffer, text)
				.construct();
		result.addAll(translatedEntries);
	}

	private String connectTranslation(List<SubtitleEntry> selectionBuffer) {
		val text = getSelectionText(selectionBuffer);
		val context = buildContext(selectionBuffer);
		val translatedText = translationConnector.translate(text, EN, BG, context);
		return StringEscapeUtils.unescapeHtml4(translatedText);
	}

	private String getSelectionText(List<SubtitleEntry> selectionBuffer) {
		return String.join(" ", selectionBuffer.stream().map(SubtitleEntry::text).toList());
	}

	private String buildContext(List<SubtitleEntry> selectionBuffer) {
		val bufferIndex = selections.indexOf(selectionBuffer);
		val offset = selections.size() - bufferIndex < 3 ? 4 : 2;
		val startIndex = Math.max(0, bufferIndex - offset);
		val endIndex = Math.min(selections.size() - 1, startIndex + 5);
		return IntStream.range(startIndex, endIndex)
				.mapToObj(selections::get)
				.map(this::getSelectionText)
				.collect(Collectors.joining(" "));
	}

}
