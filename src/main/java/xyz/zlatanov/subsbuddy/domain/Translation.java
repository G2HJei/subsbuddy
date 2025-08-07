package xyz.zlatanov.subsbuddy.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;
import static xyz.zlatanov.subsbuddy.domain.Translation.Status.CREATED;

import java.util.UUID;

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
	private UUID	id;
	private int		sourceHashCode; // todo move to other class?
	private UUID	sourceSubtitleId;
	private UUID	translatedSubtitleId;
	@NotNull
	@Enumerated(STRING)
	private Status	status	= CREATED;

	public enum Status {
		CREATED, IN_PROGRESS, COMPLETED, FAILED
	}
}
