package xyz.zlatanov.subsbuddy.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
@Entity(name = "movie_subtitles")
public class MovieSubtitle {

	@Id
	@GeneratedValue(strategy = UUID)
	private UUID				id;
	private String				filename;
	@Enumerated(STRING)
	private Language			language;
	@Column(columnDefinition = "TEXT")
	private UUID				translatedFromSubtitleId;
	private TranslationStatus	status;
	private String				subtitleData;
	private int					hashCode;
}
