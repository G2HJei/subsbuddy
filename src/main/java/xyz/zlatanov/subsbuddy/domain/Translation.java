package xyz.zlatanov.subsbuddy.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.CREATED;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Entity(name = "translation")
public class Translation {

	@Id
	@GeneratedValue(strategy = UUID)
	private String	id;
	private String	sourceId;
	private int		sourceHashCode;
	private String	translationId;
	@NotNull
	@Enumerated(STRING)
	private Status	status	= CREATED;

	public enum Status {
		CREATED, IN_PROGRESS, COMPLETED, FAILED
	}
}
