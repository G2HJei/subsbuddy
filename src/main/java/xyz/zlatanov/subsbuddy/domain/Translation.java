package xyz.zlatanov.subsbuddy.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Document
public class Translation {

	private String	id;
	private String	sourceId;
	private String translatedId;
}
