package xyz.zlatanov.subsbuddy.query.parselines;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@Data
@Accessors(fluent = true)
public class ParseLinesProjection {

	private List<SubtitleEntry> lineList = new ArrayList<>();
}
