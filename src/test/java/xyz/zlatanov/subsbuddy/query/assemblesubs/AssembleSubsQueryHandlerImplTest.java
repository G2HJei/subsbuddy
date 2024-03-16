package xyz.zlatanov.subsbuddy.query.assemblesubs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import lombok.val;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

class AssembleSubsQueryHandlerImplTest {

	AssembleSubsQueryHandler handler = new AssembleSubsQueryHandlerImpl();

	@Test
	void execute_simpleLines_assembles() {
		val query = new AssembleSubsQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 0, 100_000_000))
								.end(LocalTime.of(0, 0, 0, 900_000_000))
								.text("You're gonna be alright."),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 2))
								.end(LocalTime.of(0, 0, 3))
								.text("Yes."),
						new SubtitleEntry()
								.start(LocalTime.of(0, 0, 5))
								.end(LocalTime.of(0, 0, 6))
								.text("I feel dead.")));
		val projection = handler.execute(query);
		assertEquals("""
				1
				00:00:00,100 --> 00:00:00,900
				You're gonna be alright.

				2
				00:00:02,000 --> 00:00:03,000
				Yes.

				3
				00:00:05,000 --> 00:00:06,000
				I feel dead.

				""", projection.content());
	}

	@Test
	void execute_longLine_assemblesWithLineBreak() {
		val query = new AssembleSubsQuery()
				.linesList(List.of(
						new SubtitleEntry()
								.start(LocalTime.of(1, 7, 0))
								.end(LocalTime.of(1, 7, 5))
								.text("I can't lie to you about your chances, but... you have my sympathies.")));
		val projection = handler.execute(query);
		assertEquals("""
				1
				01:07:00,000 --> 01:07:05,000
				I can't lie to you about your chances,
				but... you have my sympathies.

				""", projection.content());
	}

}