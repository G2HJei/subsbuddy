package xyz.zlatanov.subsbuddy.core.client.assemblesubs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static xyz.zlatanov.subsbuddy.core.TestUtils.entries;

import org.junit.jupiter.api.Test;

import lombok.val;

class SrtSubsAssemblerTest {

	SubsAssembler assembler = new SrtSubsAssembler();

	@Test
	void shouldAssembleSimpleLines() {
		val actual = assembler.assemble(entries(
				"00,100 -> 0,900 -> You're gonna be alright.",
				"02 -> 3 -> Yes.",
				"05 -> 6 -> I feel dead."));

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

				""", actual);
	}

	@Test
	void shouldAssembleSubsWithLineBreak() {
		val actual = assembler.assemble(
				entries("01:07:00 -> 01:07:05 -> I can't lie to you about your chances, but... you have my sympathies."));

		assertEquals("""
				1
				01:07:00,000 --> 01:07:05,000
				I can't lie to you about your chances,
				but... you have my sympathies.

				""", actual);
	}

}