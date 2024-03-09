package xyz.zlatanov.subsbuddy.query.splitlines;

import java.time.LocalTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SplitLine {

	private LocalTime	start;
	private LocalTime	end;
	private String		text;
}
