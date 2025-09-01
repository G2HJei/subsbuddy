package xyz.zlatanov.subsbuddy.core.util;

import static java.nio.charset.StandardCharsets.UTF_8;
import static lombok.AccessLevel.PRIVATE;

import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Pattern;

import lombok.NoArgsConstructor;
import lombok.val;

@NoArgsConstructor(access = PRIVATE)
public class ReadUtils {

	private static final Pattern englishLetters = Pattern.compile("[a-zA-Z]");

	public static String readUtf8Bytes(byte[] bytes) {
		if (bytes.length == 0) {
			throw new IllegalCharsetNameException("Empty input.");
		}
		return new String(bytes, UTF_8);
	}

	public static boolean hasEnglishCharacters(String str, double threshold) {
		val strTrim = str.replaceAll("\\s+", "");
		if (strTrim.isEmpty()) {
			return false;
		}
		var englishCount = 0L;
		val englishCountTarget = threshold * strTrim.length();
		val matcher = englishLetters.matcher(strTrim);
		while (matcher.find()) {
			if (++englishCount >= englishCountTarget) {
				return true;
			}
		}
		return false;
	}
}
