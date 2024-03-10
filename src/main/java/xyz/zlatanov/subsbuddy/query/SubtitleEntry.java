package xyz.zlatanov.subsbuddy.query;

import java.time.LocalTime;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SubtitleEntry {

	private LocalTime	start;
	private LocalTime	end;
	private String		text	= "";
}
