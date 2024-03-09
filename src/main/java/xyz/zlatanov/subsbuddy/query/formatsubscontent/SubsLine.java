package xyz.zlatanov.subsbuddy.query.formatsubscontent;

import java.time.LocalTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SubsLine {

	private LocalTime	start;
	private LocalTime	end;
	private String		text;
}
