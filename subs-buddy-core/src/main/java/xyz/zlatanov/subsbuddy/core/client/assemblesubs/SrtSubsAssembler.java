package xyz.zlatanov.subsbuddy.core.client.assemblesubs;

import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import lombok.val;
import opennlp.tools.tokenize.WhitespaceTokenizer;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public class SrtSubsAssembler implements SubsAssembler {

	@Override
	public String assemble(List<SubtitleEntry> entries) {
		val sb = new StringBuilder();
		var counter = 1;
		for (SubtitleEntry entry : entries) {
			sb.append(counter++).append("\n");
			sb.append(formatTime(entry.start())).append(" --> ")
					.append(formatTime(entry.end())).append("\n");
			sb.append(improveReadability(entry.text())).append("\n\n");
		}
		return sb.toString();
	}

	private static String formatTime(LocalTime time) {
		return String.format("%02d:%02d:%02d,%03d",
				time.getHour(), time.getMinute(), time.getSecond(), time.getNano() / 1000000);
	}

	private String improveReadability(String t) {
		val readabilityThreshold1 = 40;
		val readabilityThreshold2 = 45;
		if (t.length() < readabilityThreshold2) {
			return t;
		}
		if (t.length() * 2 < readabilityThreshold1 * 2) {
			return splitText(t, readabilityThreshold1);
		}
		if (t.length() * 2 < readabilityThreshold2 * 2) {
			return splitText(t, readabilityThreshold2);
		}
		return splitText(t, readabilityThreshold1);
	}

	private String splitText(String text, int lineSize) {
		final Queue<String> tokens = new LinkedList<>(List.of(WhitespaceTokenizer.INSTANCE.tokenize(text)));
		var result = "";
		var sizeCounter = 0;
		while (!tokens.isEmpty()) {
			val token = tokens.poll();
			if (sizeCounter + token.length() + 1 < lineSize) {
				sizeCounter += token.length() + 1;
				result += token + " ";
			} else {
				sizeCounter = token.length() + 1;
				result = removeLastWhitespace(result);
				result += "\n" + token + " ";
			}
		}
		return removeLastWhitespace(result);
	}

	private static String removeLastWhitespace(String result) {
		return result.substring(0, result.length() - 1);
	}
}
