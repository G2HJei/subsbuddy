package xyz.zlatanov.subsbuddy.web.domain;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Data;
import lombok.experimental.Accessors;
import xyz.zlatanov.subsbuddy.core.domain.Language;

@Data
@Accessors(fluent = true)
@Entity(name = "movie_subtitles")
public class MovieSubtitle {

	@Id
	@GeneratedValue(strategy = UUID)
	private UUID				id;
	private String				name;
	@Enumerated(STRING)
	private Language			language;
	private UUID				translatedFromSubtitleId;
	@Enumerated(STRING)
	private TranslationStatus	status;
	@Column(columnDefinition = "TEXT")
	private String				subtitleData;
	private int					hashCode;
	private LocalDateTime		createdAt	= LocalDateTime.now();
}
