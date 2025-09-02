package xyz.zlatanov.subsbuddy.core;

import static java.lang.Integer.parseInt;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public class TestUtils {

	public static List<SubtitleEntry> entries(String... entries) {
		return Arrays.stream(entries)
				.map(TestUtils::entry)
				.toList();
	}

	public static SubtitleEntry entry(String entry) {
		val parts = entry.split(" -> ", 3);
		return new SubtitleEntry()
				.start(time(parts[0].trim()))
				.end(time(parts[1].trim()))
				.text(parts[2]);
	}

	private static LocalTime time(String str) {
		val parts = str.split(":");
		val fullFormat = parts.length == 3;
		assert fullFormat || parts.length == 1;
		val hour = fullFormat ? parseInt(parts[0]) : 0;
		val min = fullFormat ? parseInt(parts[1]) : 0;
		val secAndNano = (fullFormat ? parts[2] : parts[0]).split(",");
		val sec = parseInt(secAndNano[0]);
		val milli = secAndNano.length > 1 ? parseInt(secAndNano[1]) : 0;
		return LocalTime.of(hour, min, sec, milli * 1_000_000);
	}
}
