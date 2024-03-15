package xyz.zlatanov.subsbuddy.query.parselines;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import lombok.val;

class ParseLinesQueryHandlerImplTest {

	ParseLinesQueryHandler handler = new ParseLinesQueryHandlerImpl();

	@Test
	void execute_validSub_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				The world is changed.

				2
				00:00:35,300 --> 00:00:38,200
				I feel it in the water.

				3
				00:00:38,900 --> 00:00:41,700
				I feel it in the earth.
				"""));
		assertEquals(3, split.lineList().size());
		val firstLine = split.lineList().getFirst();
		assertEquals(LocalTime.of(0, 0, 32, 200000000), firstLine.start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), firstLine.end());
		assertEquals("The world is changed.", firstLine.text());

		val secondLine = split.lineList().get(1);
		assertEquals(LocalTime.of(0, 0, 35, 300000000), secondLine.start());
		assertEquals(LocalTime.of(0, 0, 38, 200000000), secondLine.end());
		assertEquals("I feel it in the water.", secondLine.text());

		val thirdLIne = split.lineList().getLast();
		assertEquals(LocalTime.of(0, 0, 38, 900000000), thirdLIne.start());
		assertEquals(LocalTime.of(0, 0, 41, 700000000), thirdLIne.end());
		assertEquals("I feel it in the earth.", thirdLIne.text());
	}

	@Test
	void execute_subWithMetadata_removesMetadata() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				Title: Lord of the Rings: The Fellowship of the Ring
				Director: Peter Jackson

				1
				00:00:32,200 --> 00:00:35,100
				The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_subNoActualText_removesLine() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:12,200 --> 00:00:15,100
				♪ ♪

				2
				00:00:32,200 --> 00:00:35,100
				The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_subWithAmbienceInline_removesAmbience() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				[LOTR music plays]

				2
				00:00:40,100 --> 00:00:45,200
				The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_subDotMillis_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32.200 --> 00:00:35.100
				The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32, 200000000), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), split.lineList().getFirst().end());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_noMillis_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32 --> 00:00:35
				The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35), split.lineList().getFirst().end());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_multiLine_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				The world is changed.
				I feel it in the water.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32, 200000000), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), split.lineList().getFirst().end());
		assertEquals("The world is changed. I feel it in the water.", split.lineList().getFirst().text());
	}

	@Test
	void execute_multiLineWithNewLines_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				The world is changed.

				I feel it in the water.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32, 200000000), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), split.lineList().getFirst().end());
		assertEquals("The world is changed. I feel it in the water.", split.lineList().getFirst().text());
	}

	@Test
	void execute_htmlTagsString_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				<i>The <b>world</b> is changed.</i>
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32, 200000000), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), split.lineList().getFirst().end());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_squareBracketsString_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				[Music] The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32, 200000000), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), split.lineList().getFirst().end());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}

	@Test
	void execute_asterisksBracketsString_splits() {
		val split = handler.execute(new ParseLinesQuery().subtitleData("""
				1
				00:00:32,200 --> 00:00:35,100
				*Music* The world is changed.
				"""));
		assertEquals(1, split.lineList().size());
		assertEquals(LocalTime.of(0, 0, 32, 200000000), split.lineList().getFirst().start());
		assertEquals(LocalTime.of(0, 0, 35, 100000000), split.lineList().getFirst().end());
		assertEquals("The world is changed.", split.lineList().getFirst().text());
	}
}