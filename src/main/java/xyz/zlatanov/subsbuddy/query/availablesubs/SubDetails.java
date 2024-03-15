package xyz.zlatanov.subsbuddy.query.availablesubs;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.domain.Language;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class SubDetails {

	private String					id;
	private String					filename;
	private String					language;
	private Map<Language, String>	translations	= new LinkedHashMap<>();
}
