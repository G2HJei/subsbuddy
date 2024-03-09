package xyz.zlatanov.subsbuddy.query.formatsubscontent;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class FormatSubsContentQuery {

	private List<SubsLine> linesList = new ArrayList<>();
}
