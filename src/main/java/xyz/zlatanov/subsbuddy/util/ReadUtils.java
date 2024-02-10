package xyz.zlatanov.subsbuddy.util;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.nio.charset.IllegalCharsetNameException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.val;

public class ReadUtils {

	private static final Pattern englishLetters = Pattern.compile("[a-zA-Z]");

	public static String readUtf8Bytes(byte[] bytes) {
		if (bytes.length == 0) {
			throw new IllegalCharsetNameException("Empty input.");
		}
		return new String(bytes, UTF_8);
	}

	public static boolean hasEnglishCharacters(String str, double threshold) {
		val strTrim = str.replace(" ", "");
		Matcher matcher = englishLetters.matcher(strTrim);
		var englishCount = 0L;
		while (matcher.find()) {
			englishCount++;
		}
		return ((double) englishCount / strTrim.length()) >= threshold;
	}
}
