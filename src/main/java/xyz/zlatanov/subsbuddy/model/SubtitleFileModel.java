package xyz.zlatanov.subsbuddy.model;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class SubtitleFileModel {

	private String				id;
	private String				filename;
	private String				language;
	private Map<String, String>	translations	= new LinkedHashMap<>();
}
