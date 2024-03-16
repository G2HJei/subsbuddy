package xyz.zlatanov.subsbuddy.domain;

import static xyz.zlatanov.subsbuddy.domain.Translation.Status.CREATED;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Document
public class Translation {

	private String	id;
	private String	sourceId;
	private int		sourceHashCode;
	private String	translatedId;
	private Status	status	= CREATED;

	public enum Status {
		CREATED, IN_PROGRESS, COMPLETED, FAILED
	}
}
