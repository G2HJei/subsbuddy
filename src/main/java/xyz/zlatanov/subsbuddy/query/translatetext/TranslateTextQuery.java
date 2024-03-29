package xyz.zlatanov.subsbuddy.query.translatetext;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@Data
@Accessors(fluent = true)
public class TranslateTextQuery {

	private List<SubtitleEntry> linesList = new ArrayList<>();
}
