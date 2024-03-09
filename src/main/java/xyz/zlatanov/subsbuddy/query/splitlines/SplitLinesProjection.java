package xyz.zlatanov.subsbuddy.query.splitlines;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SplitLinesProjection {

	private List<SplitLine> lineList = new ArrayList<>();
}
