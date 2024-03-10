package xyz.zlatanov.subsbuddy.query.splitlines;

import static org.springframework.util.StringUtils.hasText;
import static xyz.zlatanov.subsbuddy.util.ReadUtils.hasEnglishCharacters;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.val;

@Service
public class SplitLinesQueryHandlerImpl implements SplitLinesQueryHandler {

	private final String	time		= "([\\d:.,]+) --> ([\\d:.,]+)";
	private final Pattern	timePattern	= Pattern.compile(time, Pattern.DOTALL);

	@Override
	public SplitLinesProjection execute(SplitLinesQuery query) {
		final List<SplitLine> lineList = new ArrayList<>();
		val scanner = new Scanner(query.subtitleData());
		var splitLine = new SplitLine();
		while (scanner.hasNextLine()) {
			val textLine = scanner.nextLine();
			if (hasText(textLine)) { // skip empty lines
				val matcher = timePattern.matcher(textLine);
				if (matcher.matches()) { // next subtitle entry reached
					lineList.add(splitLine);
					splitLine = new SplitLine();

					splitLine.start(LocalTime.parse(matcher.group(1).replace(',', '.')));
					splitLine.end(LocalTime.parse(matcher.group(2).replace(',', '.')));

				} else { // add a text line of current subtitle entry
					splitLine.text((splitLine.text() + textLine).trim() + "\n");
				}
			}
		}
		lineList.add(splitLine);
		return new SplitLinesProjection()
				.lineList(lineList.stream()
						.filter(l -> l.start() != null && l.end() != null) // remove metadata and text before first subtitle entry
						.filter(l -> hasEnglishCharacters(l.text(), 0))
						.map(l -> l.text(trimLine(l.text())))
						.filter(l -> hasText(l.text()))
						.toList());
	}

	private static String trimLine(String input) {
		if (input.isEmpty()) {
			return input;
		}
		String[] lines = input.split("\r?\n");
		final List<String> chunks = new ArrayList<>();
		for (int i = 0; i < lines.length; i++) {
			var line = lines[i];
			if ((i == lines.length - 1 && line.matches("\\d+"))// last line is the line number of the next subtitle entry
					|| !StringUtils.hasLength(line)
					|| (line.startsWith("[") && line.endsWith("]"))) { // ambience text
				continue;
			}
			chunks.add(line);
		}
		return String.join("\n", chunks);
	}
}
