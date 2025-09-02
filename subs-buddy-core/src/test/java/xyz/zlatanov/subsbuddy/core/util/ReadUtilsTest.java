package xyz.zlatanov.subsbuddy.core.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReadUtilsTest {

	@Test
	void shouldReadValidUtf8Bytes() {
		Assertions.assertDoesNotThrow(() -> ReadUtils.readUtf8Bytes("valid".getBytes(UTF_8)));
	}

	@Test
	void shouldThrowOnEmptyBytes() {
		assertThrows(IllegalArgumentException.class, () -> ReadUtils.readUtf8Bytes(new byte[] {}));
	}

	@Test
	void shouldReadEnglishChars() {
		assertTrue(ReadUtils.hasEnglishCharacters("Hulk Hogan", 1));
	}

	@Test
	void shouldReadOverThreshold() {
		assertTrue(ReadUtils.hasEnglishCharacters("Hulk 2024", 0.5));
	}

	@Test
	void shouldThrowUnderThreshold() {
		assertFalse(ReadUtils.hasEnglishCharacters("7357", 0));
		assertFalse(ReadUtils.hasEnglishCharacters("Hulk 12345", 0.5));
	}

}