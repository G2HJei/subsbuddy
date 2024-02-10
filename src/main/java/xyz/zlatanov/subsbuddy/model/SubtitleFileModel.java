package xyz.zlatanov.subsbuddy.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class SubtitleFileModel {

	private String	id;
	private String	filename;
	private String	language;
}
