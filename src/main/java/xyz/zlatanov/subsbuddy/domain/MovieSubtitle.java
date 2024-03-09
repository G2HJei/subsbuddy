package xyz.zlatanov.subsbuddy.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(fluent = true)
@Document
public class MovieSubtitle {

	@Id
	private String		id;
	private String		filename;
	private Language	language;
	private String		subtitleData;
	@Nullable
	private String		owner;

	//todo add hash
}
