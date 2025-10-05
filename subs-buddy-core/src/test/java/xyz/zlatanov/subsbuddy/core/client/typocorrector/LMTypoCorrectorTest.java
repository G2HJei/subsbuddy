package xyz.zlatanov.subsbuddy.core.client.typocorrector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

class LMTypoCorrectorTest {

	TypoCorrector corrector = new LMTypoCorrector();

	@ParameterizedTest
	@MethodSource("testEntries")
	void shouldFixTypos(String input, String expected) {
		val actual = corrector.fix(new SubtitleEntry().text(input));
		assertEquals(expected, actual.text());
	}

	static Stream<Arguments> testEntries() {
		val argsList = List.of(
				// I -> l
				"terribIe",
				"terrible",

				"you'II be safe",
				"you'll be safe",

				"You Iook old",
				"You look old",

				"What? I think.",
				"What? I think.",

				"Stop! I said.",
				"Stop! I said.",

				"Yes. I am",
				"Yes. I am",

				"So am I. Thank you.",
				"So am I. Thank you.",

				"It was nice meeting you.",
				"It was nice meeting you.",

				"I'm here.",
				"I'm here.",

				// l -> I
				"l'm here.",
				"I'm here.",

				"So am l. Thank you.",
				"So am I. Thank you.",

				"l'll be there for you",
				"I'll be there for you"

		);
		val args = new ArrayList<Arguments>();
		for (int i = 0; i < argsList.size(); i = i + 2) {
			args.add(arguments(argsList.get(i), argsList.get(i + 1)));
		}
		return args.stream();
	}
}