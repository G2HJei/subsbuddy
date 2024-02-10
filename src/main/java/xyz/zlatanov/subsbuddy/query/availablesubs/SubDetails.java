package xyz.zlatanov.subsbuddy.query.availablesubs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
public class SubDetails {

	private String	id;
	private String	filename;
	private String	language;
}
