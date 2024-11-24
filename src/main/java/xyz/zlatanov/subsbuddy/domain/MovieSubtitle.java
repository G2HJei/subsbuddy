package xyz.zlatanov.subsbuddy.domain;

import jakarta.persistence.*;

import lombok.Data;
import lombok.experimental.Accessors;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.UUID;

@Data
@Accessors(fluent = true)
@Entity(name = "movie_subtitles")
public class MovieSubtitle {

	@Id
	@GeneratedValue(strategy = UUID)
	private String		id;
	private String		filename;
	@Enumerated(STRING)
	private Language	language;
	@Column(columnDefinition = "TEXT")
	private String		subtitleData;
	private String		owner;
}
