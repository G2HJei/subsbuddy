package xyz.zlatanov.subsbuddy.core.query.translatetext;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.core.query.SubtitleEntry;

@Data
@Accessors(fluent = true)
public class TranslateTextProjection {

	private List<SubtitleEntry> linesList = new ArrayList<>();
}
