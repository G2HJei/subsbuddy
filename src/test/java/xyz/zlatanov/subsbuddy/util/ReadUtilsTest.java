package xyz.zlatanov.subsbuddy.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ReadUtilsTest {

	@Test
	void readUtf8Bytes_validBytes_returns() {
		assertDoesNotThrow(() -> ReadUtils.readUtf8Bytes("valid".getBytes(UTF_8)));
	}

	@Test
	void readUtf8Bytes_empty_throws() {
		assertThrows(IllegalArgumentException.class, () -> ReadUtils.readUtf8Bytes(new byte[] {}));
	}

	@Test
	void verifyEnglishChars_onlyEnglish_returns() {
		assertTrue(ReadUtils.hasEnglishCharacters("Hulk Hogan", 1));
	}

	@Test
	void verifyEnglishChars_overThreshold_returns() {
		assertTrue(ReadUtils.hasEnglishCharacters("Hulk 2024", 0.5));
	}

	@Test
	void verifyEnglishChars_underThreshold_throws() {
		assertFalse(ReadUtils.hasEnglishCharacters("7357", 0.1));
		assertFalse(ReadUtils.hasEnglishCharacters("Hulk 12345", 0.5));
	}

}