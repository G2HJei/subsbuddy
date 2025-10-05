package xyz.zlatanov.subsbuddy.core.client.typocorrector;

import lombok.val;
import xyz.zlatanov.subsbuddy.core.domain.SubtitleEntry;

public class LMTypoCorrector implements TypoCorrector {

	public static final String	MIDDLE_I	= "(?<=[a-z])I(?=[a-z])";
	public static final String	STARTING_I	= "(?<![.?!]) I(?=[a-z]+\\b)";
	public static final String	STARTING_L	= "(?<=^| )l(?=['.?!])";

	@Override
	public SubtitleEntry fix(SubtitleEntry entry) {
		val correctedText = entry.text()
				.replaceAll(MIDDLE_I, "l")
				.replaceAll(STARTING_I, " l")
				.replace("II", "ll")
				.replaceAll(STARTING_L, "I");
		return entry.text(correctedText);
	}
}
