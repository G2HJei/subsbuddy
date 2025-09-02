package xyz.zlatanov.subsbuddy.core.domain;

import java.time.LocalTime;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@EqualsAndHashCode
public class SubtitleEntry {

	private LocalTime	start;
	private LocalTime	end;
	private String		text	= "";
}
