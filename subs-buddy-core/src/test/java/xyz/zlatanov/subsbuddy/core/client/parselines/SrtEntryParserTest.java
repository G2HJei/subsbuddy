package xyz.zlatanov.subsbuddy.core.client.parselines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser.INFO_ENTRY;
import static xyz.zlatanov.subsbuddy.core.client.parselines.SrtEntryParser.INFO_LINE;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

class SrtEntryParserTest {

	EntryParser					parser					= new SrtEntryParser();

	static final SubtitleEntry	THE_WORLD_IS_CHANGED	= new SubtitleEntry()
			.start(LocalTime.of(0, 0, 32, 200000000))
			.end(LocalTime.of(0, 0, 35, 100000000))
			.text("The world is changed.");

	@ParameterizedTest
	@MethodSource("splitLinesArgs")
	void shouldSplitLines(String subtitleData, List<SubtitleEntry> expectedLines) {
		val lineList = parser.parse(subtitleData, false);
		assertEquals(expectedLines, lineList);
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
						List.of(
								THE_WORLD_IS_CHANGED,
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 35, 300000000))
										.end(LocalTime.of(0, 0, 38, 200000000))
										.text("I feel it in the water."),
								new SubtitleEntry()
										.start(LocalTime.of(0, 0, 38, 900000000))
										.end(LocalTime.of(0, 0, 41, 700000000))
										.text("I feel it in the earth."))),

				Arguments.argumentSet(
						"Remove metadata",
						"""
								Title: Lord of the Rings: The Fellowship of the Ring
								Director: Peter Jackson

								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

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
						List.of(THE_WORLD_IS_CHANGED)),

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
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"With milliseconds",
						"""
								1
								00:00:32.200 --> 00:00:35.100
								The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"No milliseconds",
						"""
								1
								00:00:32 --> 00:00:35
								The world is changed.
								""",
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 32))
								.end(LocalTime.of(0, 0, 35))
								.text("The world is changed."))),

				Arguments.argumentSet(
						"Multiline",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								I feel it in the water.
								""",
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 32, 200000000))
								.end(LocalTime.of(0, 0, 35, 100000000))
								.text("The world is changed. I feel it in the water."))),

				Arguments.argumentSet(
						"New line",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.

								I feel it in the water.
								""",
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 32, 200000000))
								.end(LocalTime.of(0, 0, 35, 100000000))
								.text("The world is changed. I feel it in the water."))),

				Arguments.argumentSet(
						"HTML tags",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								<i>The <b>world</b> is changed.</i>
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"Square bracket tags",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								[Music] The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.argumentSet(
						"Asterisk tags",
						"""
								1
								00:00:32,200 --> 00:00:35,100
								*Music* The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)));
	}

	@Test
	void shouldAddInfoLine() {
		val lineList = parser.parse("""
				1
				00:00:32,200 --> 00:00:35,100
				*Music* The world is changed.
				""",
				true);
		assertEquals(List.of(INFO_ENTRY, THE_WORLD_IS_CHANGED), lineList);
	}

	@Test
	void shouldMergeFirstLineWithInfoLineIfStartIsTooSoon() {
		val lineList = parser.parse("""
				1
				00:00:04,000 --> 00:00:05,000
				*Music* The world is changed.
				""",
				true);

		val expectedLineList = List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0, 0))
						.end(LocalTime.of(0, 0, 4, 0))
						.text(INFO_LINE),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 4, 0))
						.end(LocalTime.of(0, 0, 5, 0))
						.text(INFO_LINE + "\nThe world is changed."));
		assertEquals(expectedLineList, lineList);
	}
}