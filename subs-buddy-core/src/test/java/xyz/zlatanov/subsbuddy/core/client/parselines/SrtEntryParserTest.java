package xyz.zlatanov.subsbuddy.core.client.parselines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xyz.zlatanov.subsbuddy.core.TestUtils.entries;
import static xyz.zlatanov.subsbuddy.core.TestUtils.entry;
import static xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser.INFO_ENTRY;
import static xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser.INFO_LINE;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

class SrtEntryParserTest {

	EntryParser			parser					= new SrtEntryParser();

	static final String	THE_WORLD_IS_CHANGED	= "32,200 -> 35,100 -> The world is changed.";

	@ParameterizedTest
	@MethodSource("splitLinesArgs")
	void shouldSplitLines(String subtitleData, List<SubtitleEntry> expectedEntries) {
		val actualEntries = parser.parse(subtitleData, false);
		assertEquals(expectedEntries, actualEntries);
	}

	static Stream<Arguments> splitLinesArgs() {
		return Stream.of(
				Arguments.argumentSet(
						"Multiple lines",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.

								2
								00:00:35,300 --> 00:00:38,200
								I feel it in the water.

								3
								00:00:38,900 --> 00:00:41,700
								I feel it in the earth.
								""",
						entries(
								THE_WORLD_IS_CHANGED,
								"35,300 -> 38,200 -> I feel it in the water.",
								"38,900 -> 41,700 -> I feel it in the earth.")),

				Arguments.argumentSet(
						"Remove metadata",
						"""
								Title: Lord of the Rings: The Fellowship of the Ring
								Director: Peter Jackson

								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						entries(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"No actual text",
						"""
								1
								00:00:12,200 --> 00:00:15,100
								♪ ♪

								2
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						entries(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"With inline ambience",
						"""
								1
								00:00:12,200 --> 00:00:15,100
								[LOTR music plays]

								2
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						entries(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"With milliseconds",
						"""
								1
								00:00:32.200 --> 00:00:35.100
								The world is changed.
								""",
						entries(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"No milliseconds",
						"""
								1
								00:00:32 --> 00:00:35
								The world is changed.
								""",
						entries("32 -> 35 -> The world is changed.")),

				Arguments.argumentSet(
						"Multiline",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								I feel it in the water.
								""",
						entries("32,200 -> 35,100 -> The world is changed. I feel it in the water.")),

				Arguments.argumentSet(
						"New line",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.

								I feel it in the water.
								""",
						entries("32,200 -> 35,100 -> The world is changed. I feel it in the water.")),

				Arguments.argumentSet(
						"HTML tags",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								<i>The <b>world</b> is changed.</i>
								""",
						entries(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"Square bracket tags",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								[Music] The world is changed.
								""",
						entries(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"Asterisk tags",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								*Music* The world is changed.
								""",
						entries(THE_WORLD_IS_CHANGED)));
	}

	@Test
	void shouldAddInfoLine() {
		val actualEntries = parser.parse("""
				1
				00:00:32,200 --> 00:00:35,100
				*Music* The world is changed.
				""",
				true);
		assertEquals(List.of(INFO_ENTRY, entry(THE_WORLD_IS_CHANGED)), actualEntries);
	}

	@Test
	void shouldMergeFirstLineWithInfoLineIfStartIsTooSoon() {
		val actualEntries = parser.parse("""
				1
				00:00:04,000 --> 00:00:05,000
				*Music* The world is changed.
				""",
				true);

		val expectedEntries = entries(
				"0 -> 4 -> -- Translated by \uD83D\uDC3B \"Subs Buddy\" --",
				"4 -> 5 -> " + INFO_LINE + "\nThe world is changed.");
		assertEquals(expectedEntries, actualEntries);
	}
}