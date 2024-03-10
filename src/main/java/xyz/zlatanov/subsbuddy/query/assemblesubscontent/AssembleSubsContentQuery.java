package xyz.zlatanov.subsbuddy.query.assemblesubscontent;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.query.SubtitleEntry;

@Data
@Accessors(fluent = true)
public class AssembleSubsContentQuery {

	private List<SubtitleEntry> linesList = new ArrayList<>();
}
