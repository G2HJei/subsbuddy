package xyz.zlatanov.subsbuddy.query.parselines;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQueryHandlerImpl.INFO_ENTRY;
import static xyz.zlatanov.subsbuddy.query.parselines.ParseLinesQueryHandlerImpl.INFO_LINE;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.val;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

class ParseLinesQueryHandlerImplTest {

	ParseLinesQueryHandler		handler					= new ParseLinesQueryHandlerImpl();

	static final SubtitleEntry	THE_WORLD_IS_CHANGED	= new SubtitleEntry()
			.start(LocalTime.of(0, 0, 32, 200000000))
			.end(LocalTime.of(0, 0, 35, 100000000))
			.text("The world is changed.");

	@ParameterizedTest
	@MethodSource("splitLinesArgs")
	void shouldSplitLines(String subtitleData, List<SubtitleEntry> expectedLines) {
		val split = handler.execute(new ParseLinesQuery().subtitleData(subtitleData));
		assertEquals(expectedLines, split.lineList());
	}

	static Stream<Arguments> splitLinesArgs() {
		return Stream.of(
				Arguments.arguments(
						// multiple lines
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

				Arguments.arguments(
						// remove metadata
						"""
								Title: Lord of the Rings: The Fellowship of the Ring
								Director: Peter Jackson

								1
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.arguments(
						// no actual text
						"""
								1
								00:00:12,200 --> 00:00:15,100
								♪ ♪

								2
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.arguments(
						// with inline ambience
						"""
								1
								00:00:12,200 --> 00:00:15,100
								[LOTR music plays]

								2
								00:00:32,200 --> 00:00:35,100
								The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.arguments(
						// with milliseconds
						"""
								1
								00:00:32.200 --> 00:00:35.100
								The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.arguments(
						// no milliseconds
						"""
								1
								00:00:32 --> 00:00:35
								The world is changed.
								""",
						List.of(new SubtitleEntry()
								.start(LocalTime.of(0, 0, 32))
								.end(LocalTime.of(0, 0, 35))
								.text("The world is changed."))),

				Arguments.arguments(
						// multiline
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

				Arguments.arguments(
						// new line
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

				Arguments.arguments(
						// html tags
						"""
								1
								00:00:32,200 --> 00:00:35,100
								<i>The <b>world</b> is changed.</i>
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.arguments(
						// square bracket tags
						"""
								1
								00:00:32,200 --> 00:00:35,100
								[Music] The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)),

				Arguments.arguments(
						// square asterisk tags
						"""
								1
								00:00:32,200 --> 00:00:35,100
								*Music* The world is changed.
								""",
						List.of(THE_WORLD_IS_CHANGED)));
	}

	@Test
	void shouldAddInfoLine() {
		val split = handler.execute(new ParseLinesQuery()
				.addSubsBuddyInfo(true)
				.subtitleData("""
						1
						00:00:32,200 --> 00:00:35,100
						*Music* The world is changed.
						"""));
		assertEquals(List.of(INFO_ENTRY, THE_WORLD_IS_CHANGED), split.lineList());
	}

	@Test
	void shouldMergeFirstLineWithInfoLineIfStartIsTooSoon() {
		val split = handler.execute(new ParseLinesQuery()
				.addSubsBuddyInfo(true)
				.subtitleData("""
						1
						00:00:04,000 --> 00:00:05,000
						*Music* The world is changed.
						"""));
		assertEquals(List.of(
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 0, 0))
						.end(LocalTime.of(0, 0, 4, 0))
						.text(INFO_LINE),
				new SubtitleEntry()
						.start(LocalTime.of(0, 0, 4, 0))
						.end(LocalTime.of(0, 0, 5, 0))
						.text(INFO_LINE + "\nThe world is changed.")),
				split.lineList());
	}
}