package xyz.zlatanov.subsbuddy.query.parselines;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class ParseLinesQuery {

	private boolean	addSubsBuddyInfo	= false;
	private String	subtitleData;
}
