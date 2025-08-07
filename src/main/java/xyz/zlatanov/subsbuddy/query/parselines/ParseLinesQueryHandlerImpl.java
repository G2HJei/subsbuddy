package xyz.zlatanov.subsbuddy.query.parselines;

import static org.springframework.util.StringUtils.hasText;
import static xyz.zlatanov.subsbuddy.util.ReadUtils.hasEnglishCharacters;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.val;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@Service
public class ParseLinesQueryHandlerImpl implements ParseLinesQueryHandler {

	public static final String			INFO_LINE	= "--Translated by 🐻 Subs Buddy--";
	public static final SubtitleEntry	INFO_ENTRY	= new SubtitleEntry()
			.start(LocalTime.of(0, 0, 0))
			.end(LocalTime.of(0, 0, 10))
			.text(INFO_LINE);
	private final Pattern				timePattern	= Pattern.compile("([\\d:.,]+) --> ([\\d:.,]+)", Pattern.DOTALL);

	@Override
	public ParseLinesProjection execute(ParseLinesQuery query) {
		var lineList = getSubtitleEntries(query).stream()
				.filter(l -> l.start() != null && l.end() != null) // remove metadata and a text before the first subtitle entry
				.filter(l -> hasEnglishCharacters(l.text(), 0))
				.map(l -> l.text(trimLine(l.text())))
				.filter(l -> hasText(l.text()))
				.collect(Collectors.toCollection(ArrayList::new));
		if (query.addSubsBuddyInfo()) {
			addInfoLine(lineList);
		}
		return new ParseLinesProjection().lineList(lineList);
	}

	private void addInfoLine(final List<SubtitleEntry> lineList) {
		if (lineList.isEmpty() || lineList.getFirst().start().isAfter(LocalTime.of(0, 0, 10))) {
			lineList.addFirst(INFO_ENTRY);
		} else {
			val first = lineList.getFirst();
			lineList.addFirst(new SubtitleEntry()
					.start(LocalTime.of(0, 0, 0))
					.end(first.start())
					.text(INFO_LINE));
			first.text(INFO_LINE + "\n" + first.text());
		}
	}

	private List<SubtitleEntry> getSubtitleEntries(ParseLinesQuery query) {
		final List<SubtitleEntry> lineList = new ArrayList<>();
		val scanner = new Scanner(query.subtitleData());
		var splitLine = new SubtitleEntry();
		while (scanner.hasNextLine()) {
			val textLine = scanner.nextLine();
			if (hasText(textLine)) { // skip empty lines
				val matcher = timePattern.matcher(textLine);
				if (matcher.matches()) { // the next subtitle entry is reached
					lineList.add(splitLine);
					splitLine = new SubtitleEntry();

					splitLine.start(LocalTime.parse(matcher.group(1).replace(',', '.')));
					splitLine.end(LocalTime.parse(matcher.group(2).replace(',', '.')));

				} else { // add a text line of the current subtitle entry
					splitLine.text((splitLine.text() + textLine).trim() + "\n");
				}
			}
		}
		lineList.add(splitLine);
		return lineList;
	}

	private static String trimLine(String input) {
		return input
				.replaceAll("([.!?])(\\S)", "$1 $2") 	// add spaces after punctuation
				.replaceAll("\r?\n", " ") 					// new lines
				.replaceAll("\\s{2,}", " ") 				// double whitespaces
				.replaceAll("<[^>]*+>", "") 					// <b></b>
				.replaceAll("\\[[^]]*+]", "") 				// [man enters]
				.replaceAll("\\*[^\\\\*]*+\\*", "")  				// *man enters*
				.trim()
				.replaceAll("\\d+$", "")					// Neo! 123
				.trim();
	}
}
