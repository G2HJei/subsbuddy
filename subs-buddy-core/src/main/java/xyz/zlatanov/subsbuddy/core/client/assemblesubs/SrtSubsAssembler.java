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
		for (val entry : entries) {
			val startTime = formatTime(entry.start());
			val endTime = formatTime(entry.end());
			sb.append(String.format("""
					%s
					%s --> %s
					%s

					""",
					counter++,
					startTime, endTime,
					improveReadability(entry.text())));
		}
		return sb.toString();
	}

	private static String formatTime(LocalTime time) {
		return String.format("%02d:%02d:%02d,%03d",
				time.getHour(), time.getMinute(), time.getSecond(), time.getNano() / 1000000);
	}

	private String improveReadability(String t) {
		val readabilityThresholdChars = 40;
		val readabilityThresholdCharsExtended = 45;
		if (t.length() < readabilityThresholdCharsExtended) {
			return t;
		}
		val threshold = t.length() > readabilityThresholdCharsExtended * 2
				? readabilityThresholdChars
				: readabilityThresholdCharsExtended;

		return splitText(t, threshold);

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
